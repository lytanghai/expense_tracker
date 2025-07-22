package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.cache.ExpenseRecordCache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/cache")
public class CacheController {


    @PostMapping("/clear")
    public void clearCache() {
        ExpenseRecordCache.clear();
    }

}
