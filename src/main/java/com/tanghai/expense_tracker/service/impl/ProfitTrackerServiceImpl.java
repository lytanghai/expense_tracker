package com.tanghai.expense_tracker.service.impl;

import com.tanghai.expense_tracker.constant.ApplicationCode;
import com.tanghai.expense_tracker.constant.Static;
import com.tanghai.expense_tracker.dto.req.ProfitAddRequest;
import com.tanghai.expense_tracker.dto.req.ProfitDeleteRequest;
import com.tanghai.expense_tracker.dto.req.ProfitFilterRequest;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.dto.res.ProfitResponse;
import com.tanghai.expense_tracker.dto.res.ProfitTrackerListResp;
import com.tanghai.expense_tracker.entity.ProfitTracker;
import com.tanghai.expense_tracker.exception.ServiceException;
import com.tanghai.expense_tracker.repository.ProfitTrackerRepo;
import com.tanghai.expense_tracker.service.ProfitTrackerService;
import com.tanghai.expense_tracker.util.AmountUtil;
import com.tanghai.expense_tracker.util.DateUtil;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
public class ProfitTrackerServiceImpl implements ProfitTrackerService {

    private final ProfitTrackerRepo profitTrackerRepo;

    public ProfitTrackerServiceImpl(ProfitTrackerRepo profitTrackerRepo) {
        this.profitTrackerRepo = profitTrackerRepo;
    }

    @Override
    public void addNewProfitRecord(ProfitAddRequest profitAddRequest) {
        String reqCurrency = profitAddRequest.getCurrency();
        if(reqCurrency == null || reqCurrency.isEmpty())
            throw new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage());

        BigDecimal pnl = profitAddRequest.getPnl();
        if(pnl == null || pnl.compareTo(BigDecimal.ZERO) <= 0)
            throw new ServiceException(ApplicationCode.W002.getCode(), ApplicationCode.W002.getMessage());

        String convertAmount = convertAmount(
                pnl.toString(),
                reqCurrency
        );

        ProfitTracker profitTracker = new ProfitTracker();
        profitTracker.setDate(ObjectUtils.isEmpty(profitAddRequest.getDate())
                ? DateUtil.format(new Date())
                : DateUtil.format(profitAddRequest.getDate()));
        profitTracker.setCategory(profitAddRequest.getCategory());
        profitTracker.setCurrency(reqCurrency);
        profitTracker.setConvertedCurrency(convertAmount.substring(0,3));
        profitTracker.setCategory(profitAddRequest.getCategory());
        profitTracker.setPnl(pnl);
        profitTracker.setPnlType(profitAddRequest.getPnlType());
        profitTracker.setNote(ObjectUtils.isEmpty(profitAddRequest.getNote())
                ? null
                : profitAddRequest.getNote());

        String convertedAmt = convertAmount.substring(4).replace(Static.COMMA,Static.EMPTY);
        profitTracker.setConvertedPrice(convertedAmt.contains(Static.PERIOD)
                ? BigDecimal.valueOf(Double.parseDouble(convertAmount.substring(4)))
                : BigDecimal.valueOf(Long.parseLong(convertedAmt)));

        profitTrackerRepo.save(profitTracker);
    }

    @Override
    public void deleteByIdOrDate(ProfitDeleteRequest profitDeleteRequest) {
        int id = ObjectUtils.isEmpty(profitDeleteRequest.getId()) ? 0 : profitDeleteRequest.getId();
        if(id != 0) {
            profitTrackerRepo.deleteById(id);
        } else {
            String inputDate = profitDeleteRequest.getDate();
            String[] range = DateUtil.getDayDateRange(inputDate);
            profitTrackerRepo.deleteAll(profitTrackerRepo.findAllByDateRange(range[0], range[1]));
        }
    }

    @Override
    public void updateProfitById(Integer id, ProfitAddRequest profitAddRequest) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException(ApplicationCode.W004.getCode(), ApplicationCode.W004.getMessage());
        }

        ProfitTracker existProfit = profitTrackerRepo.findById(id)
                .orElseThrow(() -> new ServiceException(ApplicationCode.W005.getCode(), ApplicationCode.W005.getMessage()));

        // Update fields only if present (PATCH behavior)
        if (profitAddRequest.getCategory() != null) {
            existProfit.setCategory(profitAddRequest.getCategory());
        }

        if (profitAddRequest.getNote() != null) {
            existProfit.setNote(profitAddRequest.getNote());
        }

        if (profitAddRequest.getCurrency() != null) {
            existProfit.setCurrency(profitAddRequest.getCurrency());
        }

        if (profitAddRequest.getPnlType() != null) {
            existProfit.setPnlType(profitAddRequest.getPnlType());
        }

        BigDecimal reqAmount = profitAddRequest.getPnl();
        String reqCurrency = profitAddRequest.getCurrency();

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

            existProfit.setPnl(reqAmount);
            existProfit.setConvertedPrice(
                    convertedAmt.contains(Static.PERIOD)
                            ? BigDecimal.valueOf(Double.parseDouble(convertAmount.substring(4)))
                            : BigDecimal.valueOf(Long.parseLong(convertedAmt))
            );
            existProfit.setConvertedCurrency(convertAmount.substring(0, 3));
        }

        // Save updated record
        profitTrackerRepo.save(existProfit);
    }

    @Override
    public void cleanup() {
        profitTrackerRepo.truncateAndResetSequence(Static.PROFIT_TRACKER_SEQ);
    }

    @Override
    public Page<ProfitResponse> getFilteredProfit(ProfitFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("date").descending());

        return profitTrackerRepo.findAll(this.filter(request), pageable)
                .map(ProfitResponse::new);
    }

    @Override
    public ResponseBuilder<PaginatedResponse<ProfitTracker>> fetchMonthlyProfit(String month, int page, int size) {
        if(month.isEmpty()) {
            month = DateUtil.format(new Date());
            month = DateUtil.convertToYearMonth(month);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ProfitTracker> profitPage = profitTrackerRepo.findByMonth(month, pageable);

        if(!profitPage.getContent().isEmpty()) {
            List<ProfitTracker> expenseDTOs = profitPage.getContent()
                    .stream()
                    .map(profit -> new ProfitTracker(
                            profit.getId(),
                            profit.getPnlType(),
                            profit.getCategory(),
                            profit.getPnl(),
                            profit.getDate(),
                            profit.getCurrency()
                    ))
                    .collect(Collectors.toList());

            PaginatedResponse<ProfitTracker> paginated = new PaginatedResponse<>();
            paginated.setContent(expenseDTOs);
            paginated.setPage(profitPage.getNumber());
            paginated.setSize(profitPage.getSize());
            paginated.setTotalElements(profitPage.getTotalElements());
            paginated.setTotalPages(profitPage.getTotalPages());
            paginated.setLast(profitPage.isLast());
            return ResponseBuilder.success(paginated);
        }
        return ResponseBuilder.success(null);
    }

    @Override
    public ResponseBuilder<PaginatedResponse<ProfitTracker>> fetchProfitByDate(String date1, String date2, int page, int size) {
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
            Page<ProfitTracker> expensePage = profitTrackerRepo.findByDateRange(startDate, endDate, pageable);

            if (!expensePage.getContent().isEmpty()) {
                List<ProfitTracker> expenseDTOs = expensePage.getContent()
                        .stream()
                        .map(expense -> new ProfitTracker(
                                expense.getId(),
                                expense.getPnlType(),
                                expense.getCategory(),
                                expense.getPnl(),
                                expense.getDate(),
                                expense.getCurrency()
                        ))
                        .collect(Collectors.toList());

                PaginatedResponse<ProfitTracker> paginated = new PaginatedResponse<>();
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
    public ResponseBuilder<ProfitTrackerListResp> fetchDaily(boolean b) {


        String[] range = DateUtil.getDayDateRange(new Date());
        List<ProfitTracker> data = profitTrackerRepo.findAllByDateRange(range[0], range[1]);


        // Build response
        ProfitTrackerListResp response = new ProfitTrackerListResp();
        response.setResult(data);
        response.setTotalItem(data.size());

        List<ProfitTracker> usdTransaction = data.stream()
                .filter(i -> i.getCurrency().equals(Static.USD))
                .collect(Collectors.toList());

        List<ProfitTracker> khrTransaction = data.stream()
                .filter(i -> i.getCurrency().equals(Static.KHR))
                .collect(Collectors.toList());

        // Sum USD and KHR separately (safe even if list is empty)
        BigDecimal totalUsdAmountInTransaction = usdTransaction.stream()
                .map(ProfitTracker::getPnl)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalKhrAmountInTransaction = khrTransaction.stream()
                .map(ProfitTracker::getPnl)
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

    public static String convertAmount(String amount, String currency) {
        BigDecimal value = new BigDecimal(amount);
        return currency.equals(Static.USD)
                ? AmountUtil.getDisplayAmountKHR(Static.KHR, value.multiply(Static.USD_TO_KHR_RATE))
                : AmountUtil.getDisplayAmountUSD(Static.USD, value.divide(Static.USD_TO_KHR_RATE, 2, RoundingMode.HALF_UP));
    }

    public static Specification<ProfitTracker> filter(ProfitFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getCategory() != null && !req.getCategory().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("category")), "%" + req.getCategory().toLowerCase() + "%"));
            }

            if (req.getPnlType() != null && !req.getPnlType().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("pnl_type")), "%" + req.getPnlType().toLowerCase() + "%"));
            }

            if (req.getCurrency() != null && !req.getCurrency().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("currency")), req.getCurrency().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
