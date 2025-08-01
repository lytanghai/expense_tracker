package com.tanghai.expense_tracker.entity;

import com.tanghai.expense_tracker.constant.Static;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "saving_plan")
public class SavingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Static.SAVING_PLAN_SEQ)
    @SequenceGenerator(name = Static.SAVING_PLAN_SEQ, sequenceName = Static.SAVING_PLAN_SEQ, allocationSize = 1)
    private Integer id;

    @Column(name = "plan_name", nullable = false, length = 100)
    private String planName;

    @Column(name = "target_amount", nullable = false)
    private BigDecimal targetAmount;

    @Column(name = "target_amount_currency", nullable = false)
    private String targetCurrency;

    @Column(name = "current_amount")
    private BigDecimal currentAmount;

    @Column(name = "current_amount_currency", nullable = false)
    private String currentAmountCurrency;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "savingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepositHistory> depositHistories;

    public SavingPlan() {
    }

    public SavingPlan(Integer id, String planName, BigDecimal targetAmount, String targetCurrency, BigDecimal currentAmount, String currentAmountCurrency, Date deadline, Date createdAt, String status) {
        this.id = id;
        this.planName = planName;
        this.targetAmount = targetAmount;
        this.targetCurrency = targetCurrency;
        this.currentAmount = currentAmount;
        this.currentAmountCurrency = currentAmountCurrency;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getCurrentAmountCurrency() {
        return currentAmountCurrency;
    }

    public void setCurrentAmountCurrency(String currentAmountCurrency) {
        this.currentAmountCurrency = currentAmountCurrency;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DepositHistory> getDepositHistories() {
        return depositHistories;
    }

    public void setDepositHistories(List<DepositHistory> depositHistories) {
        this.depositHistories = depositHistories;
    }
}
