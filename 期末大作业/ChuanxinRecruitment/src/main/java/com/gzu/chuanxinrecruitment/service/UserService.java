package com.gzu.chuanxinrecruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public interface UserService {

    public List<User> getAllUser();

    IPage<User> getUserPage(Integer currentPage, Integer pageSize); // 添加分页查询方法

    public User getUserById(Integer id);

    public User getUserByUsername(String username);

    public boolean addUser(User user);

    public boolean updateUser(User user);

    public boolean deleteUser(Integer id);
    // 用户登录验证
    public String login(String username, String password, String role);
    // 用户注册
    public String register(User user);

    List<Map<String, Object>> getUserGrowthData(LocalDateTime startDate);

}