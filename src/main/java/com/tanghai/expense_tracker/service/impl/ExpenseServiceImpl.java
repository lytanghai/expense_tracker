package com.tanghai.expense_tracker.service.impl;

import com.tanghai.expense_tracker.cache.ExpenseRecordCache;
import com.tanghai.expense_tracker.constant.ApplicationCode;
import com.tanghai.expense_tracker.dto.req.ExpenseAddRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.res.ExpenseTrackerListResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.exception.ServiceException;
import com.tanghai.expense_tracker.repository.ExpenseTrackerRepo;
import com.tanghai.expense_tracker.service.ExpenseService;
import com.tanghai.expense_tracker.util.DateUtil;
import com.tanghai.expense_tracker.util.ExchangeRateUtil;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseTrackerRepo expenseTrackerRepo;

    public ExpenseServiceImpl(ExpenseTrackerRepo expenseTrackerRepo) {
        this.expenseTrackerRepo = expenseTrackerRepo;
    }

    @Override
    public void addNewExpenseRecord(ExpenseAddRequest expenseAddRequest) {
        String createdAt = ObjectUtils.isEmpty(expenseAddRequest.getCreatedAt())
                ? DateUtil.format(new Date())
                : DateUtil.format(expenseAddRequest.getCreatedAt());

        String note = ObjectUtils.isEmpty(expenseAddRequest.getNote())
                ? null
                : expenseAddRequest.getNote();

        String reqCurrency = expenseAddRequest.getCurrency();
        if(reqCurrency == null || reqCurrency.isEmpty())
            throw new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage());

        BigDecimal reqAmount = expenseAddRequest.getPrice();
        if(reqAmount == null)
            throw new ServiceException(ApplicationCode.W002.getCode(), ApplicationCode.W002.getMessage());

        String convertAmount = ExchangeRateUtil.convertAmount(
                reqAmount.toString(),
                reqCurrency
        );

        ExpenseTracker expenseTracker = new ExpenseTracker();
        expenseTracker.setExpenseDate(createdAt);
        expenseTracker.setCategory(expenseTracker.getCategory());
        expenseTracker.setCurrency(reqCurrency);

        String convertedAmt = convertAmount.substring(4).replace(",","");
        expenseTracker.setConvertedPrice(convertedAmt.contains(".")
                ? BigDecimal.valueOf(Double.parseDouble(convertAmount.substring(4)))
                : BigDecimal.valueOf(Long.parseLong(convertedAmt)));

        expenseTracker.setConvertedCurrency(convertAmount.substring(0,3));
        expenseTracker.setCategory(expenseAddRequest.getCategory());
        expenseTracker.setItem(expenseAddRequest.getItem());
        expenseTracker.setPrice(reqAmount);
        expenseTracker.setNote(note);
        expenseTrackerRepo.save(expenseTracker);

        //clear cache
        ExpenseRecordCache.clear();
    }

    @Override
    public void cleanup() {
        expenseTrackerRepo.deleteAll();
    }

    @Override
    public void deleteByIdOrDate(ExpenseDeleteRequest expenseDeleteRequest) {
        int id = expenseDeleteRequest.getId();
        if(!ObjectUtils.isEmpty(id)) {
            expenseTrackerRepo.deleteById(id);
        } else {
            String inputDate = expenseDeleteRequest.getDate();
            String[] range = DateUtil.getDayDateRange(inputDate);
            expenseTrackerRepo.deleteAll(expenseTrackerRepo.findAllByDateRange(range[0], range[1]));
        }
    }

    @Override
    public ResponseBuilder<ExpenseTrackerListResp> fetchDaily(boolean enableCache) {
        List<ExpenseTracker> data;

        // Fetch from cache if enabled and available
        if (enableCache && !ExpenseRecordCache.isEmpty()) {
            data = ExpenseRecordCache.getAll();
        } else {
            String[] range = DateUtil.getDayDateRange(new Date());
            data = expenseTrackerRepo.findAllByDateRange(range[0], range[1]);

            // Populate cache if enabled
            if (enableCache && !data.isEmpty()) {
                ExpenseRecordCache.init(data);
            }
        }

        // Build response
        ExpenseTrackerListResp response = new ExpenseTrackerListResp();
        response.setResult(data);
        response.setTotalItem(data.size());

        Map<String, BigDecimal> totals = data.stream()
                .collect(Collectors.groupingBy(
                        ExpenseTracker::getCurrency,
                        Collectors.reducing(BigDecimal.ZERO, ExpenseTracker::getPrice, BigDecimal::add)
                ));

        response.setTotalAmountInUSD(totals.getOrDefault("USD", BigDecimal.ZERO));
        response.setTotalAmountInKHR(totals.getOrDefault("KHR", BigDecimal.ZERO));

        return new ResponseBuilder<ExpenseTrackerListResp>().success(response);
    }


    @Override
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> fetchExpenses(String month, int page, int size) {
        if(month.isEmpty()) {
            month = DateUtil.format(new Date());
            month = DateUtil.convertToYearMonth(month);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ExpenseTracker> expensePage = expenseTrackerRepo.findByMonth(month, pageable);

        if(!expensePage.getContent().isEmpty()) {
            List<ExpenseTracker> expenseDTOs = expensePage.getContent()
                    .stream()
                    .map(expense -> new ExpenseTracker(
                            expense.getId(),
                            expense.getItem(),
                            expense.getCategory(),
                            expense.getPrice(),
                            expense.getExpenseDate(),
                            expense.getCurrency()
                    ))
                    .collect(Collectors.toList());

            PaginatedResponse<ExpenseTracker> paginated = new PaginatedResponse<>();
            paginated.setContent(expenseDTOs);
            paginated.setPage(expensePage.getNumber());
            paginated.setSize(expensePage.getSize());
            paginated.setTotalElements(expensePage.getTotalElements());
            paginated.setTotalPages(expensePage.getTotalPages());
            paginated.setLast(expensePage.isLast());
            return ResponseBuilder.success(paginated);
        }
        return ResponseBuilder.success(null);
    }

}
