/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blstream.myhoard.biz.exception;

import org.codehaus.jackson.annotate.JsonProperty;

public class ErrorCode {

    private int errorCode;
    private String errorReason;

    public ErrorCode() {
    }

    public ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode(int errorCode, String errorReason) {
        this.errorCode = errorCode;
        this.errorReason = errorReason;
    }

    @JsonProperty(value = "error_code")
    public int getErrorCode() {
        return errorCode;
    }

    @JsonProperty(value = "error_code")
    public void setErrorCode(int error_code) {
        this.errorCode = error_code;
    }

    @JsonProperty(value = "error_reason")
    public String getErrorReason() {
        return errorReason;
    }

    @JsonProperty(value = "error_reason")
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}
