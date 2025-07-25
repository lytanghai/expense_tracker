package com.tanghai.expense_tracker.repository.impl;

import com.tanghai.expense_tracker.constant.Static;
import com.tanghai.expense_tracker.repository.ExpenseTrackerCustomRepo;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ExpenseTrackerCustomRepoImpl implements ExpenseTrackerCustomRepo {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void truncateAndResetSequence(String entity) {
        if (Static.EXPENSE_TRACKER_SEQ.equals(entity)) {
            entityManager.createNativeQuery("DELETE FROM expense_tracker").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE expense_tracker_id_seq RESTART WITH 1").executeUpdate();
        } else if(Static.PROFIT_TRACKER_SEQ.equals(entity)) {
            entityManager.createNativeQuery("DELETE FROM profit_tracker").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE profit_tracker_id_seq RESTART WITH 1").executeUpdate();
        }

    }
}
