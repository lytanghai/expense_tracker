package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.dto.req.DepositRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.req.SavingPlanCreateReq;
import com.tanghai.expense_tracker.dto.res.DepositHistory;
import com.tanghai.expense_tracker.dto.res.DepositResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.SavingPlan;
import com.tanghai.expense_tracker.service.SavingPlanService;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saving_plan")
public class SavingPlanController {

    private final SavingPlanService savingPlanService;

    public SavingPlanController(SavingPlanService savingPlanService) {
        this.savingPlanService = savingPlanService;
    }

    @PostMapping("/create_plan")
    public void createSavingPlan(@RequestBody SavingPlanCreateReq req) {
        savingPlanService.createSavingPlan(req);
    }

    @GetMapping("/view_plans")
    public ResponseBuilder<PaginatedResponse<SavingPlan>> responseMonthlyExpenses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return savingPlanService.viewSavingPlans(page, size);
    }

    @PostMapping("/deposit")
    public ResponseBuilder<DepositResp> deposit(@RequestBody DepositRequest request) {
        return savingPlanService.deposit(request);
    }

    @GetMapping("/check_history")
    public ResponseBuilder<PaginatedResponse<DepositHistory>> history(@RequestParam(defaultValue = "0") int planId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10")int size) {
        return savingPlanService.history(planId, page, size);
    }

    @PostMapping("/delete_plan")
    public void delete(@RequestBody ExpenseDeleteRequest deleteRequest) {
        savingPlanService.deletePlan(deleteRequest);
    }
}
