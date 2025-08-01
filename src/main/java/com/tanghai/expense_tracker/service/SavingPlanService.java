package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.dto.req.DepositRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.req.SavingPlanCreateReq;
import com.tanghai.expense_tracker.dto.res.DepositHistory;
import com.tanghai.expense_tracker.dto.res.DepositResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.SavingPlan;
import com.tanghai.expense_tracker.util.ResponseBuilder;

public interface SavingPlanService {
    void createSavingPlan(SavingPlanCreateReq req);
    void deletePlan(ExpenseDeleteRequest req);
    ResponseBuilder<PaginatedResponse<SavingPlan>> viewSavingPlans(int page, int size);
    ResponseBuilder<DepositResp> deposit(DepositRequest request);
    ResponseBuilder<PaginatedResponse<DepositHistory>> history(int planId, int page, int size);
}
