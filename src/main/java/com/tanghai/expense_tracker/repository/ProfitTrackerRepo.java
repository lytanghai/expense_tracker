package com.tanghai.expense_tracker.repository;

import com.tanghai.expense_tracker.dto.res.CurrencyTotalProjection;
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

    @Query(value = "SELECT et.currency, SUM(et.pnl) AS total\n" +
            "    FROM profit_tracker et\n" +
            "    WHERE TO_TIMESTAMP(et.date, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "          BETWEEN TO_TIMESTAMP(:start, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "              AND TO_TIMESTAMP(:end, 'DD-MM-YYYY HH24:MI:SS')\n" +
            "    GROUP BY et.currency\n" +
            "    ORDER BY et.currency"
            , nativeQuery = true)
    List<CurrencyTotalProjection> calculateTransactionDate(
            @Param("start") String start,
            @Param("end") String end
    );

    @Query(
            value = "SELECT et.currency, SUM(et.pnl) AS total " +
                    "FROM profit_tracker et " +
                    "WHERE TO_CHAR(TO_TIMESTAMP(et.date, 'DD-MM-YYYY HH24:MI:SS'), 'YYYY-MM') = :month GROUP BY et.currency",
            countQuery = "SELECT COUNT(*) " +
                    "FROM profit_tracker et " +
                    "WHERE TO_CHAR(TO_TIMESTAMP(et.date, 'DD-MM-YYYY HH24:MI:SS'), 'YYYY-MM') = :month",
            nativeQuery = true
    )
    List<CurrencyTotalProjection> calculateTransactionPerMonth(String month);
}
