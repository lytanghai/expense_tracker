package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.dto.req.ExpenseAddRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.res.ExpenseTrackerListResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.util.ResponseBuilder;

import java.util.List;

public interface ExpenseService {

    ResponseBuilder<PaginatedResponse<ExpenseTracker>> fetchExpenses(String month, int page, int size);
    ResponseBuilder<ExpenseTrackerListResp> fetchDaily(boolean enableCache);

    void deleteByIdOrDate(ExpenseDeleteRequest expenseDeleteRequest);
    void addNewExpenseRecord(ExpenseAddRequest expenseAddRequest);
    void cleanup();
}
