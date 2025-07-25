package com.tanghai.expense_tracker.repository;

import com.tanghai.expense_tracker.entity.ProfitTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfitTrackerRepo extends JpaRepository<ProfitTracker, Integer> , ExpenseTrackerCustomRepo, JpaSpecificationExecutor<ProfitTracker> {

    @Query(value = "SELECT * FROM profit_tracker \n" +
            "    WHERE to_char(to_timestamp(date, 'DD-MM-YYYY HH24:MI:SS'), 'YYYY-MM') = :month",
            countQuery = "SELECT count(*) FROM profit_tracker \n" +
                    "WHERE to_char(to_timestamp(date, 'DD-MM-YYYY HH24:MI:SS'), 'YYYY-MM') = :month",
            nativeQuery = true)
    Page<ProfitTracker> findByMonth(String month, Pageable pageable);

    @Query(value = "SELECT * FROM profit_tracker " +
            "WHERE to_timestamp(date, 'DD-MM-YYYY HH24:MI:SS') BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp)",
            countQuery = "SELECT count(*) FROM profit_tracker " +
                    "WHERE to_timestamp(date, 'DD-MM-YYYY HH24:MI:SS') BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp)",
            nativeQuery = true)
    Page<ProfitTracker> findByDateRange(@Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         Pageable pageable);


    @Query(value = "SELECT * FROM profit_tracker\n" +
            "    WHERE to_timestamp(date, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "          BETWEEN to_timestamp(:startDate, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "              AND to_timestamp(:endDate, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "    ORDER BY to_timestamp(date, 'DD-MM-YYYY HH24:MI:SS') DESC", nativeQuery = true)
    List<ProfitTracker> findAllByDateRange(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}
