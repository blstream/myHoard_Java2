package com.blstream.myhoard.biz.exception;

import com.mysql.jdbc.MysqlErrorNumbers;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public final class MyHoardException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;
    private final int responseStatus;
    private final Map<String, String> customErrors = new HashMap<>();

    /**
     * Tworzy wyjątek z podanym kodem błędu, pustym opisem i statusem HTTP 400.
     * @param errorCode kod błędu
     */
    @Deprecated
    public MyHoardException(int errorCode) {
        super(null, null);
        this.errorCode = errorCode;
        this.errorMessage = "";
        this.responseStatus = HttpServletResponse.SC_BAD_REQUEST;
    }

    /**
     * Tworzy wyjątek z podanym kodem błędu i opisem oraz statusem HTTP 400.
     * @param errorCode kod błędu
     * @param errorMessage opis błędu
     */
    @Deprecated
    public MyHoardException(int errorCode, String errorMessage) {
        super(null, null);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.responseStatus = HttpServletResponse.SC_BAD_REQUEST;
    }

    /**
     * Tworzy wyjątek z podanym kodem błędu, opisem oraz statusem (znajdującym
     * się w klasie @ref(HttpServletResponse).
     * @param errorCode kod błędu
     * @param errorMessage opis błędu
     * @param responseStatus status HTTP
     */
    @Deprecated
    public MyHoardException(int errorCode, String errorMessage, int responseStatus) {
        super(null, null);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.responseStatus = responseStatus;
    }

    public MyHoardException(ErrorCode errorCode) {
        super(null, null);
        this.errorCode = errorCode.getErrorCode();
        this.responseStatus = errorCode.getHttpStatus();
        this.errorMessage = errorCode.getErrorMessage();
    }

    /**
     * Konstruktor parsujący obiekt typu HibernateException.
     * @see <a href="https://dev.mysql.com/doc/refman/5.0/en/error-messages-server.html">https://dev.mysql.com/doc/refman/5.0/en/error-messages-server.html</a>
     * @param exception
     */
    public MyHoardException(HibernateException exception) {
        this(201, "Validation error");
        if (exception instanceof JDBCException) {
            SQLException e = ((JDBCException)exception).getSQLException();
            while (e != null) {
                String detailMessage = e.toString();
                switch (e.getErrorCode()) {
                    case MysqlErrorNumbers.ER_DUP_ENTRY:
                        detailMessage = detailMessage.substring(detailMessage.indexOf(':') + 2, detailMessage.length() - 1);
                        add(detailMessage.substring(detailMessage.lastIndexOf('\'') + 1), detailMessage.substring(0, detailMessage.indexOf("' ") + 1));
                        break;
                    case MysqlErrorNumbers.ER_DATA_TOO_LONG:
                        detailMessage = detailMessage.substring(detailMessage.indexOf(':') + 2);
                        add(detailMessage.substring(detailMessage.indexOf('\'') + 1, detailMessage.lastIndexOf('\'')), "Data too long");
                        break;

                    default:
                        add(detailMessage.substring(0, detailMessage.indexOf(':')), detailMessage.substring(detailMessage.indexOf(':') + 2));
                }
                e = e.getNextException();
            }
        }
    }

    /**
     * Metoda powoduje dodanie błędu do mapy. Jeśli dane pole występuje w mapie,
     * stara wartość <i>reason</i> zostanie podmieniona (jednemu polu odpowiada
     * jeden opis błędu).
     * @param field nazwa niepoprawnego pola,
     * @param reason powód błędu.
     * @return referencja <i>this</i>, by można było utworzyć łańcuch.
     */
    public MyHoardException add(String field, String reason) {
        customErrors.put(field, reason);
        return this;
    }

    public MyHoardError toError() {
        MyHoardError error = new MyHoardError();
        error.errorCode = errorCode;
        error.errorMessage = errorMessage;
        error.errors = customErrors;
        return error;
    }

    public int getResponseStatus() {
        return responseStatus;
    }
}
