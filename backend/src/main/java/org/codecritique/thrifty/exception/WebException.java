package org.codecritique.thrifty.exception;

public class WebException extends RuntimeException {

    public WebException(Throwable th) {
        super(th);
    }
}
