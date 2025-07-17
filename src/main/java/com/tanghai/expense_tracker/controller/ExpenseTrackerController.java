package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.dto.req.ExpenseAddRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.res.ExpenseTrackerListResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.service.ExpenseService;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseTrackerController {

    private final ExpenseService expenseService;

    public ExpenseTrackerController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/create")
    public void addNewExpense(@RequestBody ExpenseAddRequest expenseAddRequest) {
        expenseService.addNewExpenseRecord(expenseAddRequest);
    }

    @PostMapping("/delete")
    public void deleteExpenseByIdOrDate(@RequestBody ExpenseDeleteRequest expenseDeleteRequest) {
        expenseService.deleteByIdOrDate(expenseDeleteRequest);  // e.g., "2025-07-12" for date
    }

    /**
     * Fetch By monthly
     * month = 2025-07
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-monthly")
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> responseExpenses(
            @RequestParam(defaultValue = "") String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return expenseService.fetchExpenses(month, page, size);
    }

    /**
     * Fetch By daily
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-daily")
    public ResponseBuilder<ExpenseTrackerListResp> responseExpensesDaily() {
        return expenseService.fetchDaily(true);
    }

    @PostMapping("/cleanup")
    public void cleanup() {
        expenseService.cleanup();
    }

}
