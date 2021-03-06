package org.codecritique.thrifty.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@RestController
public class SessionTimeoutController {
    /**
     * This GET method enables the front-end Javascript app to learn when the security session will expire.
     * The information is also available in the JSESSIONID cookie, but the cookie is Http-only.
     *
     * @return the current session timeout date expressed as the number of milliseconds since the Epoch.
     */
    @GetMapping(path = "/session-timeout", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getCurrentSessionTimeoutDate() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        long maxAge = session.getServletContext().getSessionCookieConfig().getMaxAge();
        long maxAgeMillis = maxAge * 1000;
        return Long.toString(session.getCreationTime() + maxAgeMillis);
    }
}
