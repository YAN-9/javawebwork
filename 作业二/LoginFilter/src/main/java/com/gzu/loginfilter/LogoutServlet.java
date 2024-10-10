package com.gzu.loginfilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "logoutServlet", value = "/logoutServlet") // 定义 servlet，处理 /logoutServlet 的请求
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(); // 获取当前用户的会话
        // 清除会话信息，注销用户
        session.invalidate(); // 使当前会话无效
        // 输出注销信息
        System.out.println("您已退出登录，返回首页！！！");
        // 重定向到主页
        resp.sendRedirect(req.getContextPath() + "/indexServlet"); // 重定向到 indexServlet

    }
}
