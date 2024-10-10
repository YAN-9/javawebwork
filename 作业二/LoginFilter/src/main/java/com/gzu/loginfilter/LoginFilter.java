package com.gzu.loginfilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "loginFilter", urlPatterns = "/*") // 将此过滤器应用于所有传入请求
public class LoginFilter implements Filter {

    // 不需要登录的路径列表
    private static final List<String> EXCLDED_PATH = Arrays.asList("/login", "/register", "/public");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 过滤器的初始化代码
        System.out.println("LoginFilter 初始化！");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request; // 转换为 HttpServletRequest
        HttpServletResponse httpResponse = (HttpServletResponse) response; // 转换为 HttpServletResponse

        // 获取请求URI，去除上下文路径
        String requestURI = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // 检查请求URI是否在排除路径中
        if (isExcludedPath(requestURI)) {
            chain.doFilter(request, response); // 允许请求继续
            return;
        }

        // 从会话中获取用户名
        String username = (String) httpRequest.getSession().getAttribute("username");

        // 检查用户是否已登录
        if (username != null) {
            chain.doFilter(request, response); // 用户已登录，允许请求继续
            System.out.println("用户 " + username + " 已登录！！！");
        } else {
            // 用户未登录，重定向到登录页面
            System.out.println("用户未登录，即将跳转登录界面！！！");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        }
    }

    // 检查请求路径是否在排除列表中
    private boolean isExcludedPath(String requestURI) {
        return EXCLDED_PATH.stream().anyMatch(requestURI::startsWith); // 如果找到匹配则返回true
    }

    @Override
    public void destroy() {
        // 过滤器的清理代码
        System.out.printf("LoginFilter 销毁！");
    }
}
