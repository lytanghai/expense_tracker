package com.tanghai.expense_tracker.repository;

import com.tanghai.expense_tracker.entity.SavingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingPlanRepo extends JpaRepository<SavingPlan, Integer> {
}
