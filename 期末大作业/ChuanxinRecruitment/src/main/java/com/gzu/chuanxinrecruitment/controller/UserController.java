package com.gzu.chuanxinrecruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.User;
import com.gzu.chuanxinrecruitment.service.UserService;
import com.gzu.chuanxinrecruitment.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/user")
    public Result<List<User>> getAllEmployee() {
        return Result.OK(userService.getAllUser());
    }

    // 分页查询
    @GetMapping("/user/page")
    public Result<IPage<User>> getUserPage(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        IPage<User> page = userService.getUserPage(currentPage, pageSize);
        return Result.OK(page);
    }

    // 根据id查询员工
    @GetMapping("/user/{id}")
    public Result<User> getUser(@PathVariable Integer id) {
        return Result.OK(userService.getUserById(id));
    }

    // 根据username查询员工
    @GetMapping("/user/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        return Result.OK(userService.getUserByUsername(username));
    }

    //添加
    @PostMapping("/user")
    public Result<User> addUser(@RequestBody User user) {

        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        boolean success = userService.addUser(user);

        if (success) {
            // 用户添加成功，返回成功的响应
            return Result.ok("用户添加成功");
        } else {
            // 用户添加失败，返回失败的响应
            return Result.error("用户添加失败");
        }
    }

    @PutMapping("/user/{id}")
    public Result<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        // 设置用户 ID 为路径中的 id
        user.setId(id);

        boolean success = userService.updateUser(user);
        if (success) {
            // 更新成功，返回成功的响应
            return Result.ok("用户信息更新成功");
        } else {
            // 更新失败，返回失败的响应
            return Result.error("用户信息更新失败");
        }
    }


    @DeleteMapping("/user/{id}")
    public Result<Void> deleteUser(@PathVariable Integer id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            // 删除成功，返回成功的响应
            return Result.ok("用户删除成功");
        } else {
            // 删除失败，返回失败的响应
            return Result.error("用户删除失败");
        }
    }

}
