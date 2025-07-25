package com.tanghai.expense_tracker.service.impl;

import com.tanghai.expense_tracker.cache.ExpenseRecordCache;
import com.tanghai.expense_tracker.constant.ApplicationCode;
import com.tanghai.expense_tracker.constant.Static;
import com.tanghai.expense_tracker.dto.req.ExpenseAddRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseFilterRequest;
import com.tanghai.expense_tracker.dto.res.ExpenseResponse;
import com.tanghai.expense_tracker.dto.res.ExpenseTrackerListResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.exception.ServiceException;
import com.tanghai.expense_tracker.repository.ExpenseTrackerRepo;
import com.tanghai.expense_tracker.repository.impl.ExpenseTrackerCustomRepoImpl;
import com.tanghai.expense_tracker.service.ExpenseService;
import com.tanghai.expense_tracker.util.AmountUtil;
import com.tanghai.expense_tracker.util.DateUtil;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseTrackerRepo expenseTrackerRepo;
    private final ExpenseTrackerCustomRepoImpl expenseTrackerCustomRepo;

    public ExpenseServiceImpl(ExpenseTrackerRepo expenseTrackerRepo, ExpenseTrackerCustomRepoImpl expenseTrackerCustomRepo) {
        this.expenseTrackerRepo = expenseTrackerRepo;
        this.expenseTrackerCustomRepo = expenseTrackerCustomRepo;
    }

    @Override
    public void addNewExpenseRecord(ExpenseAddRequest expenseAddRequest) {

        String reqCurrency = expenseAddRequest.getCurrency();
        if(reqCurrency == null || reqCurrency.isEmpty())
            throw new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage());

        BigDecimal reqAmount = expenseAddRequest.getPrice();
        if(reqAmount == null || reqAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException(ApplicationCode.W002.getCode(), ApplicationCode.W002.getMessage());

        String convertAmount = convertAmount(
                reqAmount.toString(),
                reqCurrency
        );

        ExpenseTracker expenseTracker = new ExpenseTracker();
        expenseTracker.setExpenseDate(ObjectUtils.isEmpty(expenseAddRequest.getCreatedAt())
                ? DateUtil.format(new Date())
                : DateUtil.format(expenseAddRequest.getCreatedAt()));
        expenseTracker.setCategory(expenseTracker.getCategory());
        expenseTracker.setCurrency(reqCurrency);
        expenseTracker.setConvertedCurrency(convertAmount.substring(0,3));
        expenseTracker.setCategory(expenseAddRequest.getCategory());
        expenseTracker.setItem(expenseAddRequest.getItem());
        expenseTracker.setPrice(reqAmount);
        expenseTracker.setNote(ObjectUtils.isEmpty(expenseAddRequest.getNote())
                ? null
                : expenseAddRequest.getNote());

        String convertedAmt = convertAmount.substring(4).replace(Static.COMMA,Static.EMPTY);
        expenseTracker.setConvertedPrice(convertedAmt.contains(Static.PERIOD)
                ? BigDecimal.valueOf(Double.parseDouble(convertAmount.substring(4)))
                : BigDecimal.valueOf(Long.parseLong(convertedAmt)));

        expenseTrackerRepo.save(expenseTracker);
        ExpenseRecordCache.add(expenseTracker); //Cache
    }

    @Override
    public void updateExpenseById(Integer id, ExpenseAddRequest expenseUpdateRequest) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException(ApplicationCode.W004.getCode(), ApplicationCode.W004.getMessage());
        }

        ExpenseTracker existExpense = expenseTrackerRepo.findById(id)
                .orElseThrow(() -> new ServiceException(ApplicationCode.W005.getCode(), ApplicationCode.W005.getMessage()));

        // Update fields only if present (PATCH behavior)
        if (expenseUpdateRequest.getCategory() != null) {
            existExpense.setCategory(expenseUpdateRequest.getCategory());
        }

        if (expenseUpdateRequest.getNote() != null) {
            existExpense.setNote(expenseUpdateRequest.getNote());
        }

        if (expenseUpdateRequest.getCurrency() != null) {
            existExpense.setCurrency(expenseUpdateRequest.getCurrency());
        }

        if (expenseUpdateRequest.getItem() != null) {
            existExpense.setItem(expenseUpdateRequest.getItem());
        }

        BigDecimal reqAmount = expenseUpdateRequest.getPrice();
        String reqCurrency = expenseUpdateRequest.getCurrency();

        // Only do conversion if both price and currency are provided
        if (reqAmount != null) {
            if (reqAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException(ApplicationCode.W002.getCode(), ApplicationCode.W002.getMessage());
            }

            if (reqCurrency == null) {
                throw new ServiceException(ApplicationCode.W003.getCode(), "Currency is required when updating price.");
            }

            // Currency conversion logic
            String convertAmount = convertAmount(reqAmount.toString(), reqCurrency);
            String convertedAmt = convertAmount.substring(4).replace(Static.COMMA, Static.EMPTY);

            existExpense.setPrice(reqAmount);
            existExpense.setConvertedPrice(
                    convertedAmt.contains(Static.PERIOD)
                            ? BigDecimal.valueOf(Double.parseDouble(convertAmount.substring(4)))
                            : BigDecimal.valueOf(Long.parseLong(convertedAmt))
            );
            existExpense.setConvertedCurrency(convertAmount.substring(0, 3));
        }

        // Save updated record
        expenseTrackerRepo.save(existExpense);

        // Update cache
        ExpenseRecordCache.removeById(id);
        ExpenseRecordCache.add(existExpense);
    }

    @Override
    @Transactional
    public void cleanup() {
        expenseTrackerCustomRepo.truncateAndResetSequence(Static.EXPENSE_TRACKER_SEQ);
        ExpenseRecordCache.clear(); //Cache
    }

    @Override
    public void deleteByIdOrDate(ExpenseDeleteRequest expenseDeleteRequest) {
        int id = ObjectUtils.isEmpty(expenseDeleteRequest.getId()) ? 0 : expenseDeleteRequest.getId();
        if(id != 0) {
            expenseTrackerRepo.deleteById(id);
            ExpenseRecordCache.removeById(id);
        } else {
            String inputDate = expenseDeleteRequest.getDate();
            String[] range = DateUtil.getDayDateRange(inputDate);
            expenseTrackerRepo.deleteAll(expenseTrackerRepo.findAllByDateRange(range[0], range[1]));
            ExpenseRecordCache.removeByDateRange(range[0], range[1]); //Cache
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

       List<ExpenseTracker> usdTransaction = data.stream()
        .filter(i -> i.getCurrency().equals(Static.USD))
        .collect(Collectors.toList());

        List<ExpenseTracker> khrTransaction = data.stream()
                .filter(i -> i.getCurrency().equals(Static.KHR))
                .collect(Collectors.toList());

        // Sum USD and KHR separately (safe even if list is empty)
        BigDecimal totalUsdAmountInTransaction = usdTransaction.stream()
                .map(ExpenseTracker::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalKhrAmountInTransaction = khrTransaction.stream()
                .map(ExpenseTracker::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Convert KHR → USD and USD → KHR
        BigDecimal totalTransactionInUsd = totalUsdAmountInTransaction.add(
                totalKhrAmountInTransaction.divide(Static.USD_TO_KHR_RATE, 2, RoundingMode.HALF_UP)
        );

        BigDecimal totalTransactionInKhr = totalKhrAmountInTransaction.add(
                totalUsdAmountInTransaction.multiply(Static.USD_TO_KHR_RATE)
        );

        response.setTotalAmountInUSD(totalTransactionInUsd);
        response.setTotalAmountInKHR(totalTransactionInKhr);

        return ResponseBuilder.success(response);
    }

    @Override
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> fetchMonthlyExpenses(String month, int page, int size) {
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

    @Override
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> fetchExpensesByDate(String date1, String date2, int page, int size) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String startDate;
            String endDate;

            if (date1 != null && !date1.isEmpty()) {
                LocalDate start = LocalDate.parse(date1, inputFormatter);
                startDate = start.atStartOfDay().format(outputFormatter);
            } else {
                return null;
            }

            if (date2 != null && !date2.isEmpty()) {
                LocalDate end = LocalDate.parse(date2, inputFormatter);
                endDate = end.atTime(23, 59, 59).format(outputFormatter);
            } else {
                // If only one date is provided, treat it as a single-day query
                LocalDate single = LocalDate.parse(date1, inputFormatter);
                endDate = single.atTime(23, 59, 59).format(outputFormatter);
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<ExpenseTracker> expensePage = expenseTrackerRepo.findByDateRange(startDate, endDate, pageable);

            if (!expensePage.getContent().isEmpty()) {
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

        } catch ( Exception e) {
            return null;
        }
    }

    @Override
    public Page<ExpenseResponse> getFilteredExpenses(ExpenseFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("expenseDate").descending());

        return expenseTrackerRepo.findAll(this.filter(request), pageable)
                .map(ExpenseResponse::new);
    }

    public static Specification<ExpenseTracker> filter(ExpenseFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getCategory() != null && !req.getCategory().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("category")), "%" + req.getCategory().toLowerCase() + "%"));
            }

            if (req.getItem() != null && !req.getItem().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("item")), "%" + req.getItem().toLowerCase() + "%"));
            }

            if (req.getCurrency() != null && !req.getCurrency().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("currency")), req.getCurrency().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static String convertAmount(String amount, String currency) {
        BigDecimal value = new BigDecimal(amount);
        return currency.equals(Static.USD)
                ? AmountUtil.getDisplayAmountKHR(Static.KHR, value.multiply(Static.USD_TO_KHR_RATE))
                : AmountUtil.getDisplayAmountUSD(Static.USD, value.divide(Static.USD_TO_KHR_RATE, 2, RoundingMode.HALF_UP));
    }

}
