package com.tanghai.expense_tracker.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *  ApiResponse<UserDTO> apiResponse = new ApiResponse.Builder<UserDTO>()
 *         .status("200")
 *         .traceId("xyz")
 *         .message("OK")
 *         .data(user)
 *         .build();
 *         */

public class ResponseBuilder<T> {
    private String status;
    private String trace_id;
    private String message;
    private String date;
    private T data;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTrace_id(String trace_id) {
        this.trace_id = trace_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseBuilder() {
    }

    public static <T> ResponseBuilder<T> success(T data) {
        ResponseBuilder<T> rb = new ResponseBuilder<>();
        rb.setStatus("success");
        rb.setMessage("OK");
        rb.setDate(DateUtil.format(new Date()));
        rb.setData(data);
        return rb;
    }

    private ResponseBuilder(Builder<T> builder) {
        this.status = builder.status;
        this.trace_id = builder.trace_id;
        this.message = builder.message;
        this.date = builder.date != null ? builder.date : getCurrentDateTime();
        this.data = builder.data;
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ✅ Static Builder Class
    public static class Builder<T> {
        private String status;
        private String trace_id;
        private String message;
        private String date;
        private T data;

        public Builder<T> status(String status) {
            this.status = status;
            return this;
        }

        public Builder<T> traceId(String trace_id) {
            this.trace_id = trace_id;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> date(String date) {
            this.date = date;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseBuilder<T> build() {
            return new ResponseBuilder<>(this);
        }
    }

    // ✅ Getters
    public String getStatus() { return status; }
    public String getTrace_id() { return trace_id; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
    public T getData() { return data; }
}
