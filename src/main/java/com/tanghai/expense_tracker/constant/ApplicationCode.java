package com.tanghai.expense_tracker.constant;

public enum ApplicationCode {

    W001("W001","Req Currency must not be nullor empty!"),
    W002("W002","Req Amount must not be null or empty!"),
    W003("W003","Req Item must not be null or empty!");

    private String code;
    private String message;

    ApplicationCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
