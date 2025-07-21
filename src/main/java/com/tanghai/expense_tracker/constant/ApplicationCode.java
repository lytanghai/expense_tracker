package com.tanghai.expense_tracker.constant;

public enum ApplicationCode {

    W001("W001","Currency must not be null or empty!"),
    W002("W002","Amount must not be null,empty, or below 0"),
    W003("W003","Item must not be null or empty!"),
    W004("W004","ID must not be null or empty!"),
    W005("W005","This record is not exist!"),
    ;

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
