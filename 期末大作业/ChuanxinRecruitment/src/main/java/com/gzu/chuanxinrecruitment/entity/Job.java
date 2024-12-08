package com.gzu.chuanxinrecruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "job")  // 表名对应数据库中的表名
public class Job {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer hrId;          // 发布职位的HR用户ID
    private String title;          // 职位标题
    private String description;    // 职位描述
    private String requirements;   // 职位要求
    private String location;       // 工作地点
    private String salaryRange;    // 薪资范围
    private Date createdAt;        // 创建时间
    private Date updatedAt;        // 最后更新时间
}
