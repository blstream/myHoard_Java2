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
public class ErrorCode {

	private int error_code;

	public ErrorCode() {}

	public ErrorCode(int errorCode) {
		this.error_code = errorCode;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
}
