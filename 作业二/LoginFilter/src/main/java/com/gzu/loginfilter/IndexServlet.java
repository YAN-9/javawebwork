package com.gzu.loginfilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "indexServlet", value = "/indexServlet") // 定义 servlet，处理 /indexServlet 的请求
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理 GET 请求，调用 doPost 方法
        this.doPost(req, resp); // 将 GET 请求转发到 doPost 方法
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理 POST 请求，重定向到 index.jsp 页面
        resp.sendRedirect(req.getContextPath() + "/index.jsp"); // 重定向到 index.jsp
    }
}
