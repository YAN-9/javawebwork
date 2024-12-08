package com.gzu.chuanxinrecruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "user")  // 表名对应数据库中的表名
public class User {
    @TableId(type = IdType.AUTO)  // 表示自增主键
    private Integer id;

    private String username;  // 用户名
    private String password;  // 加密后的密码
    private String email;     // 邮箱地址
    private String role;      // 角色（'普通用户' 或 '管理员'）

    private String identity;  // 用户身份（‘管理员’， '求职者' 或 '企业HR'）
    private Date createdAt;   // 用户创建时间
    private Date updatedAt;   // 用户最后更新时间
}
