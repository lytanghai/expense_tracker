package com.tanghai.expense_tracker.repository;

import com.tanghai.expense_tracker.entity.ExpenseTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExpenseTrackerRepo extends JpaRepository<ExpenseTracker, Integer>, ExpenseTrackerCustomRepo, JpaSpecificationExecutor<ExpenseTracker> {

    @Query(value = "SELECT * FROM expense_tracker \n" +
            "    WHERE to_char(to_timestamp(expense_date, 'DD-MM-YYYY HH24:MI:SS'), 'YYYY-MM') = :month",
            countQuery = "SELECT count(*) FROM expense_tracker \n" +
            "    WHERE to_char(to_timestamp(expense_date, 'DD-MM-YYYY HH24:MI:SS'), 'YYYY-MM') = :month",
            nativeQuery = true)
    Page<ExpenseTracker> findByMonth(String month, Pageable pageable);

    @Query(value = "SELECT * FROM expense_tracker " +
            "WHERE to_timestamp(expense_date, 'DD-MM-YYYY HH24:MI:SS') BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp)",
            countQuery = "SELECT count(*) FROM expense_tracker " +
                    "WHERE to_timestamp(expense_date, 'DD-MM-YYYY HH24:MI:SS') BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp)",
            nativeQuery = true)
    Page<ExpenseTracker> findByDateRange(@Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         Pageable pageable);


    @Query(value = "SELECT * FROM expense_tracker\n" +
            "    WHERE to_timestamp(expense_date, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "          BETWEEN to_timestamp(:startDate, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "              AND to_timestamp(:endDate, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "    ORDER BY to_timestamp(expense_date, 'DD-MM-YYYY HH24:MI:SS') DESC", nativeQuery = true)
    List<ExpenseTracker> findAllByDateRange(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    @Modifying
    @Transactional
    @Query(value = " DELETE FROM expense_tracker\n" +
            "        WHERE to_timestamp(expense_date, 'DD-MM-YYYY HH24:MI:SS') < \n" +
            "              now() - interval '3 months'", nativeQuery = true)
    int deleteRecordsOlderThanThreeMonths();
}
