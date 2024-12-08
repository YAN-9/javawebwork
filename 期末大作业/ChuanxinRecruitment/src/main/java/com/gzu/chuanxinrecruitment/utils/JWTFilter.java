package com.gzu.chuanxinrecruitment.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

        // 排除登录和注册请求，不进行 JWT 验证
        if (uri.equals("/api/auth/login") || uri.equals("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取 token 并验证
        String token = extractToken(request);

        if (token != null) {
            logger.debug("提取的 Token: {}", token);  // 调试信息
            String username = JwtUtil.getUsernameFromToken(token);
            logger.debug("从 token 获取的用户名: {}", username);  // 调试信息

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    String role = JwtUtil.getRoleFromToken(token);

                    // 验证 Token 是否有效
                    if (JwtUtil.validateToken(token, username, role)) {
                        // Token 验证通过后，构造 UserDetails 和认证对象
                        UserDetails userDetails = new User(username, "", Collections.emptyList());

                        // 创建 Authentication 对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 设置认证信息到 SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        logger.debug("认证信息已设置到 SecurityContext");
                    } else {
                        logger.warn("用户名: {} 的 Token 验证失败", username);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Token 无效或已过期。");
                        return;
                    }
                } catch (Exception e) {
                    logger.error("Token 验证时出错: {}", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token 验证失败。");
                    return;
                }
            }
        }

        // 继续执行过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 提取请求中的 Bearer Token
     *
     * @param request HttpServletRequest
     * @return 提取到的 Token 或 null
     */
    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
