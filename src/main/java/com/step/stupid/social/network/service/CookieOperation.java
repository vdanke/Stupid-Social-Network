package com.step.stupid.social.network.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface CookieOperation {

    Optional<Cookie> getCookie(HttpServletRequest request, String cookieName);

    void addCookie(HttpServletResponse response, String name, String value, int maxAge);

    void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name);
}
