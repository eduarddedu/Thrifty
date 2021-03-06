package org.codecritique.thrifty.exception;

public class WebException extends RuntimeException {

    public WebException(Exception e) {
        super(e);
        e.printStackTrace();
    }
}
