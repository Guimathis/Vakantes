package com.DevProj.Vakantes.service.interceptor;

import com.DevProj.Vakantes.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if(CookieService.getCookie(request, "usuarioId") != null){
            return true;
        }
        response.sendRedirect("/auth/login");
        return false;
    }
}
