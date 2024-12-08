package com.gzu.chuanxinrecruitment.controller;

import com.gzu.chuanxinrecruitment.utils.Result;
import com.gzu.chuanxinrecruitment.entity.User;
import com.gzu.chuanxinrecruitment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // 用户注册接口
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        // 调用注册服务
        String result = userService.register(user);
        if ("success".equals(result)) {
            return Result.ok("注册成功");
        } else {
            return Result.error("注册失败", result);
        }
    }

    // 登录接口
    @PostMapping("/login")
    public Result<?> login(@RequestParam String username, @RequestParam String password, @RequestParam String role) {

        String token = userService.login(username, password, role);  // 调用服务层验证登录

        if ("用户不存在".equals(token)) {
            return Result.error("登录失败", "用户不存在");
        }

        if ("密码错误".equals(token)) {
            return Result.error("登录失败", "密码错误");
        }

        if("角色信息错误".equals(token)) {
            return Result.error("登录失败", "角色信息错误");
        }

        return Result.OK("登录成功", token);  // 返回登录成功，并携带 Token
    }

    // 注销接口
    @PostMapping("/logout")
    public ResponseEntity<Result<?>> logout(HttpServletRequest request, HttpServletResponse response) {
        // 清空 JWT 令牌（前端操作）
        response.setHeader("Authorization", "");
        request.getSession().invalidate();
        return ResponseEntity.ok(new Result<>(200, "登出成功"));
    }

}