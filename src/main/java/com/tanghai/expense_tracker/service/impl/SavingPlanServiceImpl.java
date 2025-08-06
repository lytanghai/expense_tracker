package com.tanghai.expense_tracker.service.impl;

import com.tanghai.expense_tracker.constant.ApplicationCode;
import com.tanghai.expense_tracker.constant.Static;
import com.tanghai.expense_tracker.dto.req.DepositRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.req.SavingPlanCreateReq;
import com.tanghai.expense_tracker.dto.res.DepositResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.DepositHistory;
import com.tanghai.expense_tracker.entity.SavingPlan;
import com.tanghai.expense_tracker.exception.ServiceException;
import com.tanghai.expense_tracker.repository.DepositHistoryRepo;
import com.tanghai.expense_tracker.repository.SavingPlanRepo;
import com.tanghai.expense_tracker.service.SavingPlanService;
import com.tanghai.expense_tracker.util.DateUtil;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingPlanServiceImpl implements SavingPlanService {

    private final SavingPlanRepo repo;

    private final DepositHistoryRepo depositHistoryRepo;

    public SavingPlanServiceImpl(SavingPlanRepo repo, DepositHistoryRepo depositHistory) {
        this.repo = repo;
        this.depositHistoryRepo = depositHistory;
    }

    @Override
    @Transactional
    public void createSavingPlan(SavingPlanCreateReq req) {
        if (req.getPlanName().isEmpty() ||
            req.getAmount().compareTo(BigDecimal.ZERO) <= 0 ||
            req.getAmountCurrency().isEmpty() ||
            req.getTargetDate().isEmpty()) {
            throw new ServiceException(ApplicationCode.W008.getCode(), ApplicationCode.W008.getMessage());
        }
        SavingPlan savingPlan = new SavingPlan();
        savingPlan.setPlanName(req.getPlanName());
        savingPlan.setTargetAmount(req.getAmount());
        savingPlan.setTargetCurrency(req.getAmountCurrency());
        savingPlan.setCurrentAmount(req.getInitialAmount());
        savingPlan.setCurrentAmountCurrency(req.getInitialAmountCurrency());
        savingPlan.setDeadline(DateUtil.convertToDateWithMidnight(req.getTargetDate(), false));
        savingPlan.setCreatedAt(new Date());
        savingPlan.setStatus("ongoing");
        SavingPlan savedPlan  = repo.save(savingPlan);

        if(req.getInitialAmount().compareTo(BigDecimal.ZERO) > 0) {
            DepositRequest depositRequest = new DepositRequest();
            depositRequest.setAmount(req.getInitialAmount());
            depositRequest.setCurrency(req.getInitialAmountCurrency());
            depositRequest.setPlanId(savedPlan.getId());
            deposit(depositRequest);
        }
    }

    @Override
    public ResponseBuilder<PaginatedResponse<SavingPlan>> viewSavingPlans(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<SavingPlan> savingPlans = repo.findAll(pageable);

        if (!savingPlans.getContent().isEmpty()) {
            List<SavingPlan> savingResponse = savingPlans.getContent()
                    .stream()
                    .map(savingPlan -> new SavingPlan(
                            savingPlan.getId(),
                            savingPlan.getPlanName(),
                            savingPlan.getTargetAmount(),
                            savingPlan.getTargetCurrency(),
                            savingPlan.getCurrentAmount(),
                            savingPlan.getCurrentAmountCurrency(),
                            savingPlan.getDeadline(),
                            savingPlan.getCreatedAt(),
                            savingPlan.getStatus()
                    ))
                    .collect(Collectors.toList());

            PaginatedResponse<SavingPlan> paginated = new PaginatedResponse<>();
            paginated.setContent(savingResponse);
            paginated.setPage(savingPlans.getNumber());
            paginated.setSize(savingPlans.getSize());
            paginated.setTotalElements(savingPlans.getTotalElements());
            paginated.setTotalPages(savingPlans.getTotalPages());
            paginated.setLast(savingPlans.isLast());

            return ResponseBuilder.success(paginated);
        }
        return ResponseBuilder.success(null);
    }

    @Override
    @Transactional
    public ResponseBuilder<DepositResp> deposit(DepositRequest request) {
        SavingPlan existPlan = repo.findById(request.getPlanId()).orElse(null);

        if (existPlan == null) {
            throw new ServiceException(ApplicationCode.W006.getCode(), ApplicationCode.W006.getMessage());
        }

        BigDecimal depositAmount;
        if(!existPlan.getTargetCurrency().equals(request.getCurrency())) {
            if("KHR".equals(request.getCurrency())) {
                depositAmount = request.getAmount().divide(Static.USD_TO_KHR_RATE);
            } else {
                depositAmount = request.getAmount().multiply(Static.USD_TO_KHR_RATE);
            }
            depositAmount = existPlan.getCurrentAmount().add(depositAmount);
        } else {
            depositAmount = existPlan.getCurrentAmount().add(request.getAmount());
        }

        if(depositAmount.compareTo(existPlan.getTargetAmount()) > 0) {
            throw new ServiceException(ApplicationCode.W007.getCode(), ApplicationCode.W007.getMessage());
        }
        existPlan.setCurrentAmount(depositAmount);

        Date depositDate = new Date();
        repo.save(existPlan);

        DepositHistory depositHistory = new DepositHistory();
        depositHistory.setAmount(request.getAmount());
        depositHistory.setSavingPlan(existPlan);
        depositHistory.setCurrency(request.getCurrency());
        depositHistory.setDepositedDate(depositDate);
        depositHistoryRepo.save(depositHistory);

        DepositResp response = new DepositResp();
        response.setAmount(request.getAmount());
        response.setCurrency(request.getCurrency());
        response.setProgress(calculateProgress(existPlan.getCurrentAmount(), existPlan.getTargetAmount()));
        response.setTransactionDate(DateUtil.format(depositDate, "dd-MM-yyyy HH:mm:ss"));
        return ResponseBuilder.success(response);
    }

    @Override
    public ResponseBuilder<PaginatedResponse<com.tanghai.expense_tracker.dto.res.DepositHistory>>history(int planId, int page, int size) {


        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<DepositHistory> depositHistories = depositHistoryRepo.findAllByPlanId(planId, pageable);

        if (!depositHistories.getContent().isEmpty()) {
            List<com.tanghai.expense_tracker.dto.res.DepositHistory> savingResponse = depositHistories.getContent()
                    .stream()
                    .map(depositHistory -> new com.tanghai.expense_tracker.dto.res.DepositHistory(
                            depositHistory.getId(),
                            depositHistory.getAmount(),
                            depositHistory.getCurrency(),
                            depositHistory.getDepositedDate()
                    ))
                    .collect(Collectors.toList());

            PaginatedResponse<com.tanghai.expense_tracker.dto.res.DepositHistory> paginated = new PaginatedResponse<>();
            paginated.setContent(savingResponse);
            paginated.setPage(depositHistories.getNumber());
            paginated.setSize(depositHistories.getSize());
            paginated.setTotalElements(depositHistories.getTotalElements());
            paginated.setTotalPages(depositHistories.getTotalPages());
            paginated.setLast(depositHistories.isLast());

            return ResponseBuilder.success(paginated);
        }
        return ResponseBuilder.success(null);
    }

    @Override
    public void deletePlan(ExpenseDeleteRequest req) {
        repo.deleteById(req.getId());
    }

    public static String calculateProgress(BigDecimal currentAmount, BigDecimal targetAmount) {
        if (targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.toString();
        }
        if (currentAmount == null) {
            currentAmount = BigDecimal.ZERO;
        }

        BigDecimal progress = currentAmount
                .divide(targetAmount, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        // cap at 100
        if (progress.compareTo(new BigDecimal("100")) > 0) {
            return new BigDecimal("100.00").toString();
        }

        return progress.setScale(2, RoundingMode.HALF_UP).toString();
    }

}
