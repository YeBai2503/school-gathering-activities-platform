package com.activity.interceptor;
import com.activity.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("loggedUser");

        if (loggedInUser == null) {
            response.sendRedirect("/auth/login"); // 如果用户未登录，重定向到登录页面
            return false;
        }
        return true; // 如果用户已登录，继续处理请求
    }
}
