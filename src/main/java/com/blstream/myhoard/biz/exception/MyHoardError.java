package com.blstream.myhoard.biz.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class MyHoardError {

    int errorCode = 0;
    String errorMessage = "";
    Map<String, String> errors = new HashMap<>();

    public MyHoardError() {}

    public MyHoardError(int errorCode) {
        this.errorCode = errorCode;
    }

    public MyHoardError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public MyHoardError(int errorCode, String errorMessage, Map<String, String> errors) {
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

    @JsonSerialize(using = CustomErrorsSerializer.class)
    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    /**
     * Konwersja do formatu JSON.
     * @return JSON
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer = buffer.append("{\"error_code\":").append(errorCode).append(",\"error_message\":\"").append(errorMessage).append('"');
        if (!errors.isEmpty()) {
            buffer = buffer.append(",\"errors\":{");
            for (Entry<String, String> i : errors.entrySet())
                buffer = buffer.append('"').append(i.getKey()).append("\":\"").append(i.getValue()).append("\",");
            buffer = buffer.deleteCharAt(buffer.length() - 1).append('}');
        }
        buffer = buffer.append('}');
        return buffer.toString();
    }
}
