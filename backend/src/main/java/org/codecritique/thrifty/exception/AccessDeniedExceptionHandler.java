package org.codecritique.thrifty.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AccessDeniedExceptionHandler {
    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handleAccessDeniedException(AccessDeniedException e, @AuthenticationPrincipal UserDetails user) {
        String userInfo = String.format("[User '%s' at remote address %s:%s]", user.getUsername(), request.getRemoteAddr(), request.getRemotePort());
        String requestInfo = String.format("[%s %s]", request.getMethod(), request.getRequestURI());
        String error = String.format("%s -> %s : %s%n ", userInfo, requestInfo, e.getMessage());
        System.err.println(error);
        return error;
    }
}
