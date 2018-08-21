package org.edin.exceptions;

/**
 * Created by levitsky on 21.08.18
 */
public class SoapException extends Exception {

    private static final long serialVersionUID = 1L;

    public SoapException(String message) {
        super(message);
    }

    public SoapException(String message, Throwable cause) {
        super(message, cause);
    }
}
