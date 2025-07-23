package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.dto.req.ExpenseAddRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseDeleteRequest;
import com.tanghai.expense_tracker.dto.req.ExpenseFilterRequest;
import com.tanghai.expense_tracker.dto.res.ExpenseResponse;
import com.tanghai.expense_tracker.dto.res.ExpenseTrackerListResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;

public interface ExpenseService {

    ResponseBuilder<PaginatedResponse<ExpenseTracker>> fetchMonthlyExpenses(String month, int page, int size);
    ResponseBuilder<PaginatedResponse<ExpenseTracker>> fetchExpensesByDate(String date1, String date2, int page, int size);
    ResponseBuilder<ExpenseTrackerListResp> fetchDaily(boolean enableCache);
    Page<ExpenseResponse> getFilteredExpenses(ExpenseFilterRequest request);

    void deleteByIdOrDate(ExpenseDeleteRequest expenseDeleteRequest);
    void addNewExpenseRecord(ExpenseAddRequest expenseAddRequest);
    void updateExpenseById(Integer id, ExpenseAddRequest expenseUpdateRequest);
    void cleanup();
}
