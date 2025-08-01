package com.tanghai.expense_tracker.entity;

import com.tanghai.expense_tracker.constant.Static;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "deposit_history")
public class DepositHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Static.DEPOSIT_HISTORY_SEQ)
    @SequenceGenerator(name = Static.DEPOSIT_HISTORY_SEQ, sequenceName = Static.DEPOSIT_HISTORY_SEQ, allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SavingPlan savingPlan;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(name = "deposited_date", nullable = false)
    private Date depositedDate;

    public DepositHistory() {
    }

    public DepositHistory(Integer id, BigDecimal amount, String currency, Date depositedDate) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.depositedDate = depositedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SavingPlan getSavingPlan() {
        return savingPlan;
    }

    public void setSavingPlan(SavingPlan savingPlan) {
        this.savingPlan = savingPlan;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDepositedDate() {
        return depositedDate;
    }

    public void setDepositedDate(Date depositedDate) {
        this.depositedDate = depositedDate;
    }
}
