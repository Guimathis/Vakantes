package com.DevProj.Vakantes.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class CookieService {
    public static void setCookie(HttpServletResponse response, String key, String valor, int segundos) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie(key, URLEncoder.encode(valor, StandardCharsets.UTF_8));
        cookie.setMaxAge(segundos);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String key) throws UnsupportedEncodingException {
        String valor = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                .filter(cookie -> key.equals(cookie.getName()))
                        .findAny()).map(Cookie::getValue).orElse(null);
        if(valor != null){
            valor = URLDecoder.decode(valor, StandardCharsets.UTF_8);
            return valor;
        }
        return valor;
    }

    public static void removeCookie(HttpServletResponse response, String key) throws UnsupportedEncodingException {
        setCookie(response, key, "", 0);
    }

}
