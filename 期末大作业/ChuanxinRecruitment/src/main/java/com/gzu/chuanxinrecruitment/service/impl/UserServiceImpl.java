package com.gzu.chuanxinrecruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzu.chuanxinrecruitment.entity.User;
import com.gzu.chuanxinrecruitment.mapper.UserMapper;
import com.gzu.chuanxinrecruitment.service.UserService;
import com.gzu.chuanxinrecruitment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 查询所有员工
    @Override
    public List<User> getAllUser() {
        IPage<User> page = new Page<>();
        return userMapper.selectPageVo(page);
    }

    @Override
    public IPage<User> getUserPage(Integer currentPage, Integer pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        return userMapper.selectPage(page, null);  // 使用 MyBatis-Plus 的分页查询
    }

    @Override
    public User getUserById(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean addUser(User user) {
        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userMapper.insert(user) != 0;
    }

    @Override
    public boolean updateUser(User user){
        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userMapper.updateById(user) != 0;
    }

    @Override
    public boolean deleteUser(Integer id){
        return userMapper.deleteById(id) != 0;
    }

    // 登录验证方法
    @Override
    public String login(String username, String password, String role) {
        JwtUtil jwtUtil = new JwtUtil();
        // 根据用户名查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return "用户不存在";
        }
        // 校验密码（使用 PasswordEncoder 进行密码比对）
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "密码错误";  // 密码不匹配
        }
        // 验证角色
        if (!user.getRole().equals(role)) {
            return "角色信息错误";
        }
        // 密码正确，生成 JWT token
        return jwtUtil.generateToken(username, role);  // 返回生成的 JWT
    }

    // 用户注册方法
    @Override
    public String register(User user) {
        // 检查用户名是否为空
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return "用户名不能为空";
        }
        // 检查密码是否为空
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "密码不能为空";
        }
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser != null) {
            return "用户名已存在";  // 如果用户名已存在，返回错误信息
        }

        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 插入到数据库
        int result = userMapper.insert(user);
        if (result > 0) {
            return "success";
        } else {
            return "数据库写入失败";
        }
    }

    @Override
    public List<Map<String, Object>> getUserGrowthData(LocalDateTime startDate) {
        return userMapper.getUserGrowthData(startDate);
    }
}