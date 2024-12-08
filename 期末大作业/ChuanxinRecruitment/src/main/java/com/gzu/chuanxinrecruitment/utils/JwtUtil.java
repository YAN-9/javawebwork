package com.gzu.chuanxinrecruitment.utils;

import com.gzu.chuanxinrecruitment.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    /**
     * secret 是用于签名的密钥。这个密钥在HS256算法中起着至关重要的作用，因为它用于生成和验证消息认证码（MAC）。以下是关于 secret 的一些关键点：
     * 1. 对称密钥: 在HS256中，secret 是一个对称密钥。这意味着同一个密钥用于消息的签名和验证。发送方和接收方都必须知道并安全地共享这个密钥。
     * 2. 安全性要求: 选择一个足够复杂和随机的密钥至关重要，以防止攻击者通过暴力破解或其他手段猜测密钥。通常建议使用至少256位的密钥长度。
     * 3. 密钥管理: secret 的存储和管理需要特别注意。它应存储在安全的环境中，例如安全的密钥管理系统，避免硬编码在代码中或暴露在不安全的存储中。
     * 4. 用途: secret 用于生成JWT的签名部分，以确保JWT的完整性和真实性。接收方使用相同的密钥来验证JWT是否未被篡改。
     */
    // 使用一个固定的密钥
    private static final byte[] SECRET = Base64.getDecoder().decode("T9Z2GHfBakC74WSuRYs3ssLonX5pySA+jmY6ub5NesU=");
    private static final SecretKey KEY = new SecretKeySpec(SECRET, SignatureAlgorithm.HS256.getJcaName());
    private static final long EXPIRATION = 3600 * 1000; // 1小时

    // 生成JWT
    public static String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);  // 存储角色信息
        return Jwts.builder()
                .setSubject(username)
                .addClaims(claims)  // 添加额外的 claim 信息（角色等信息）
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // 从JWT中获取声明 -- 解析JWT
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    // 从JWT中获取用户名
    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // 获取角色
    public static String getRoleFromToken(String token) {
        return (String) getClaimsFromToken(token).get("role");  // 假设角色信息存储在 "role" 中
    }

    // 验证JWT
    public static boolean validateToken(String token, String username, String role) {
        final String tokenUsername = getUsernameFromToken(token);
        final String tokenRole = getRoleFromToken(token);
        return (tokenRole.equals(role) && tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // 检查JWT是否过期
    private static boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}

