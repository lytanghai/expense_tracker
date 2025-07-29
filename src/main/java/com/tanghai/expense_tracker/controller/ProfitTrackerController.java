package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.dto.req.ProfitAddRequest;
import com.tanghai.expense_tracker.dto.req.ProfitDeleteRequest;
import com.tanghai.expense_tracker.dto.req.ProfitFilterRequest;
import com.tanghai.expense_tracker.dto.res.ProExpTrackerResp;
import com.tanghai.expense_tracker.dto.res.PaginatedResponse;
import com.tanghai.expense_tracker.dto.res.ProfitResponse;
import com.tanghai.expense_tracker.dto.res.ProfitTrackerListResp;
import com.tanghai.expense_tracker.entity.ProfitTracker;
import com.tanghai.expense_tracker.service.ProfitTrackerService;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("profit")
public class ProfitTrackerController {

    private final ProfitTrackerService profitTrackerService;

    public ProfitTrackerController(ProfitTrackerService profitTrackerService) {
        this.profitTrackerService = profitTrackerService;
    }

    @PostMapping("/create")
    public void addNewProfit(@RequestBody ProfitAddRequest profitAddRequest) {
        profitTrackerService.addNewProfitRecord(profitAddRequest);
    }

    @PostMapping("/delete")
    public void deleteProfitByIdOrDate(@RequestBody ProfitDeleteRequest profitDeleteRequest) {
        profitTrackerService.deleteByIdOrDate(profitDeleteRequest);  // e.g., "2025-07-12" for date
    }

    @PatchMapping("/update/{id}")
    public void updateProfitById(@PathVariable("id") Integer id, @RequestBody ProfitAddRequest profitAddRequest) {
        profitTrackerService.updateProfitById(id, profitAddRequest);
    }

    @PostMapping("/cleanup")
    public void cleanup() {
        profitTrackerService.cleanup();
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ProfitResponse>> filterProfit(@RequestBody ProfitFilterRequest request) {
        return ResponseEntity.ok(profitTrackerService.getFilteredProfit(request));
    }

    /**
     * Fetch By monthly
     * month = 2025-07
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-monthly")
    public ResponseBuilder<PaginatedResponse<ProfitTracker>> responseMonthlyProfit(
            @RequestParam(defaultValue = "") String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return profitTrackerService.fetchMonthlyProfit(month, page, size);
    }

    /**
     * Fetch By date
     * date1 = 2025-07-23
     * date1 = 2025-07-23 && date2 = 2025-07-25
     * */
    @GetMapping("/fetch-by-date")
    public ResponseBuilder<PaginatedResponse<ProfitTracker>> responseProfitByDate(
            @RequestParam(name = "date1", defaultValue = "") String date1,
            @RequestParam(name = "date2", defaultValue = "") String date2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return profitTrackerService.fetchProfitByDate(date1, date2, page, size);
    }

    /**
     * Fetch By daily
     * usage: manual - schedule
     * */
    @GetMapping("/fetch-daily")
    public ResponseBuilder<ProfitTrackerListResp> responseProfitDaily() {
        return profitTrackerService.fetchDaily(false); // do not enable this
    }

    //     * month = 2025-07
    //     * date1 && date2 = 2025-07-24
    @GetMapping("/calculate")
    public ResponseBuilder<ProExpTrackerResp> getTotalAmount(@RequestParam(name = "date1", defaultValue = "") String date1,
                                                             @RequestParam(name = "date2", defaultValue = "") String date2,
                                                             @RequestParam(name = "type", defaultValue = "") String type) {
        return profitTrackerService.calculate(type, date1, date2);
    }

}
