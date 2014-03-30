package com.blstream.myhoard.biz.exception;

public enum ErrorCode {
    AUTH_BAD_CREDENTIALS(101, 401, "Bad credentials"),
    AUTH_TOKEN_NOT_PROVIDED(102, 401, "Token not provided"),
    AUTH_TOKEN_INVALID(103, 401, "Invalid token"),
    FORBIDDEN(104, 403, "Forbidden"),
    BAD_REQUEST(201, 400, "Validation error"),
    NOT_FOUND(202, 404, "Resource not found"),
    INTERNAL_SERVER_ERROR(301, 500, "Internal server error");

    private final int errorCode;
    private final int httpStatus;
    private final String errorMessage;

    private ErrorCode(int errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
