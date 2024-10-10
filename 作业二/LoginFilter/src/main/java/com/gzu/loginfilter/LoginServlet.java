package com.gzu.loginfilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/loginServlet") // 定义 servlet，处理 /loginServlet 的请求
public class LoginServlet extends HttpServlet {

    // 处理 POST 请求
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 从请求中获取用户名和密码参数
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");

        // 验证用户名和密码是否正确
        if (username.equals("admin") && password.equals("admin")) {
            // 如果用户名和密码正确，将用户名存储在会话中
            request.getSession().setAttribute("username", username);
            // 验证通过，重定向到 welcome.html 页面
            response.sendRedirect(request.getContextPath() + "/welcome.html");
        } else {
            // 如果用户名或密码不正确，打印错误信息
            System.out.println("用户 " + username + " 信息无法查询，无法登录！！！");
            // 验证失败，重定向回登录页面
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
    }
}
