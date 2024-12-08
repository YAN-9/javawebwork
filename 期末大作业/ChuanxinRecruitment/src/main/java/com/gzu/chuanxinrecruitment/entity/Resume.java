package com.gzu.chuanxinrecruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "resume")  // 表名对应数据库中的表名
public class Resume {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;       // 关联的用户ID
    private String name;          // 求职者的姓名
    private String contactInfo;   // 联系方式
    private String education;     // 教育背景
    private String experience;    // 工作经验
    private String skills;        // 技能列表
    private Date createdAt;       // 创建时间
    private Date updatedAt;       // 最后更新时间
}
