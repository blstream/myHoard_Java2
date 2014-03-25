package com.blstream.myhoard.biz.exception;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonProperty;

public class Error {

    int errorCode = 0;
    String errorMessage = "";
    Map<String, String> errors = new HashMap<>();

    public Error() {}

    public Error(int errorCode) {
        this.errorCode = errorCode;
    }

    public Error(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Error(int errorCode, String errorMessage, Map<String, String> errors) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }

    @JsonProperty(value = "error_code")
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int error_code) {
        this.errorCode = error_code;
    }

    @JsonProperty(value = "error_message")
    public String getErrorReason() {
        return errorMessage;
    }

    public void setErrorReason(String errorReason) {
        this.errorMessage = errorReason;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
