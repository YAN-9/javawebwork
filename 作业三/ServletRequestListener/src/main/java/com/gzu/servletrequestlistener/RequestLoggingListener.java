package com.gzu.servletrequestlistener;

import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

@WebListener
public class RequestLoggingListener implements ServletRequestListener {
    // 创建 Logger 实例，用于记录日志
    private static final Logger logger = Logger.getLogger(RequestLoggingListener.class.getName());

    // 当请求被初始化时调用
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        // 记录请求开始时间
        sre.getServletRequest().setAttribute("startTime", System.currentTimeMillis());
    }

    // 当请求被销毁时调用
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // 从请求中获取开始时间
        Long startTime = (Long) sre.getServletRequest().getAttribute("startTime");
        if (startTime == null) {
            // 如果找不到开始时间，记录警告信息
            logger.warning("请求开始时间未找到。");
            return;
        }

        // 计算请求处理的时间
        long duration = System.currentTimeMillis() - startTime;

        // 将 ServletRequest 转换为 HttpServletRequest 以便获取更多信息
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String clientIp = request.getRemoteAddr(); // 获取客户端 IP 地址
        String method = request.getMethod(); // 获取请求方法（GET/POST）
        String uri = request.getRequestURI(); // 获取请求的 URI
        String queryString = request.getQueryString(); // 获取查询字符串
        String userAgent = request.getHeader("User-Agent"); // 获取 User-Agent 信息

        // 使用 SimpleDateFormat 格式化请求时间为中文格式
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy 年 MM 月 dd 日 HH:mm:ss", Locale.CHINESE);
        String formattedRequestTime = sdf.format(new Date(startTime)); // 格式化时间

        // 构建日志信息
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("请求时间: ").append(formattedRequestTime)
                .append(", IP: ").append(clientIp)
                .append(", 方法: ").append(method)
                .append(", URI: ").append(uri)
                .append(", 查询: ").append(queryString)
                .append(", User-Agent: ").append(userAgent)
                .append(", 处理时长: ").append(duration).append(" ms");

        // 记录日志信息
        logger.info(logMessage.toString());
    }
}
