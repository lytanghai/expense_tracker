package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.dto.req.ExpenseAddRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseFilterRequest;
import com.tanghai.expense_tracker.dto.res.ExpenseResponse;
import com.tanghai.expense_tracker.dto.res.ExpenseTrackerListResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.service.ExpenseService;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/update/{id}")
    public void updateExpenseById(@PathVariable("id") Integer id, @RequestBody ExpenseAddRequest expenseUpdateRequest) {
        expenseService.updateExpenseById(id, expenseUpdateRequest);
    }

    @PostMapping("/delete")
    public void deleteExpenseByIdOrDate(@RequestBody ExpenseDeleteRequest expenseDeleteRequest) {
        expenseService.deleteByIdOrDate(expenseDeleteRequest);  // e.g., "2025-07-12" for date
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ExpenseResponse>> filterExpenses(@RequestBody ExpenseFilterRequest request) {
        return ResponseEntity.ok(expenseService.getFilteredExpenses(request));
    }


    /**
     * Fetch By monthly
     * month = 2025-07
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-monthly")
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> responseMonthlyExpenses(
            @RequestParam(defaultValue = "") String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return expenseService.fetchMonthlyExpenses(month, page, size);
    }

    /**
     * Fetch By date
     * date1 = 2025-07-23
     * date1 = 2025-07-23 && date2 = 2025-07-25
     * */
    @GetMapping("/fetch-by-date")
    public ResponseBuilder<PaginatedResponse<ExpenseTracker>> responseExpensesByDate(
            @RequestParam(name = "date1", defaultValue = "") String date1,
            @RequestParam(name = "date2", defaultValue = "") String date2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return expenseService.fetchExpensesByDate(date1, date2, page, size);
    }

    /**
     * Fetch By daily
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-daily")
    public ResponseBuilder<ExpenseTrackerListResp> responseExpensesDaily() {
        return expenseService.fetchDaily(false); // do not enable this
    }

    @PostMapping("/cleanup")
    public void cleanup() {
        expenseService.cleanup();
    }

}
