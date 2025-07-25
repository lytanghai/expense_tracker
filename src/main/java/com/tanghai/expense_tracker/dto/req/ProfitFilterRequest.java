package com.tanghai.expense_tracker.dto.req;

public class ProfitFilterRequest {
    private String date;
    private String category;
    private String pnlType;
    private String currency;

    public ProfitFilterRequest() {
    }

    public ProfitFilterRequest(String date, String category, String pnlType, String currency) {
        this.date = date;
        this.category = category;
        this.pnlType = pnlType;
        this.currency = currency;
    }

    private int page = 0;
    private int size = 10;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPnlType() {
        return pnlType;
    }

    public void setPnlType(String pnlType) {
        this.pnlType = pnlType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
