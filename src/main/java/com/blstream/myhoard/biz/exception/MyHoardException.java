package com.blstream.myhoard.biz.exception;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class MyHoardException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;
    private final int responseStatus;
    private final Map<String, String> customErrors = new HashMap<>();

    public MyHoardException(int errorCode) {
        super(null, null);
        this.errorCode = errorCode;
        this.errorMessage = "";
        this.responseStatus = HttpServletResponse.SC_BAD_REQUEST;
    }

    public MyHoardException(int errorCode, String errorMessage) {
        super(null, null);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.responseStatus = HttpServletResponse.SC_BAD_REQUEST;
    }

    public MyHoardException(int errorCode, String errorMsg, int responseStatus) {
        super(null, null);
        this.errorCode = errorCode;
        this.errorMessage = errorMsg;
        this.responseStatus = responseStatus;
    }

    /**
     * Metoda powoduje dodanie błędu do mapy. Jeśli dane pole występuje w mapie,
     * stara wartość <i>reason</i> zostanie podmieniona (jednemu polu odpowiada
     * jeden opis błędu).
     * @param field  nazwa niepoprawnego pola,
     * @param reason powód błędu.
     * @return referencja <i>this</i>, by można było utworzyć łańcuch.
     */
    public MyHoardException add(String field, String reason) {
        customErrors.put(field, reason);
        return this;
    }

    public Error toError() {
        Error error = new Error();
        error.errorCode = errorCode;
        error.errorMessage = errorMessage;
        error.errors = customErrors;
        return error;
    }

    public int getResponseStatus() {
        return responseStatus;
    }
}
