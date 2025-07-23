package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.dto.req.ExpenseFilterRequest;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseTrackerSpecification {

    public static Specification<ExpenseTracker> filter(ExpenseFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getCategory() != null && !req.getCategory().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("category")), "%" + req.getCategory().toLowerCase() + "%"));
            }

            if (req.getItem() != null && !req.getItem().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("item")), "%" + req.getItem().toLowerCase() + "%"));
            }

            if (req.getCurrency() != null && !req.getCurrency().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("currency")), req.getCurrency().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
