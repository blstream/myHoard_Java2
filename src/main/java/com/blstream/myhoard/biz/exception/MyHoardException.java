/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blstream.myhoard.biz.exception;

/**
 *
 * @author jklimaszewski
 */
public class MyHoardException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public MyHoardException() {
        super(null, null);
    }

    public MyHoardException(int errorCode) {
        super(null, null);
        this.errorCode = errorCode;
    }

    public MyHoardException(int errorCode, String errorMsg) {
        super(null, null);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int error_code) {
        this.errorCode = error_code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
