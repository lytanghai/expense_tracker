package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.dto.req.ProfitAddRequest;
import com.tanghai.expense_tracker.dto.req.ProfitDeleteRequest;
import com.tanghai.expense_tracker.dto.req.ProfitFilterRequest;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.dto.res.ProfitResponse;
import com.tanghai.expense_tracker.dto.res.ProfitTrackerListResp;
import com.tanghai.expense_tracker.entity.ProfitTracker;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;

public interface ProfitTrackerService {
    void addNewProfitRecord(ProfitAddRequest profitAddRequest);
    void deleteByIdOrDate(ProfitDeleteRequest profitDeleteRequest);
    void updateProfitById(Integer id, ProfitAddRequest profitAddRequest);
    void cleanup();

    Page<ProfitResponse> getFilteredProfit(ProfitFilterRequest request);
    ResponseBuilder<PaginatedResponse<ProfitTracker>> fetchMonthlyProfit(String month, int page, int size);
    ResponseBuilder<PaginatedResponse<ProfitTracker>> fetchProfitByDate(String date1, String date2, int page, int size);
    ResponseBuilder<ProfitTrackerListResp> fetchDaily(boolean b);
}
