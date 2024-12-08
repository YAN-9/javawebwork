package com.gzu.chuanxinrecruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Table(name = "interview_schedule")
public class InterviewSchedule {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer applicationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date interviewTime;
    
    private String platform;
    private String status;// '待面试', '已完成'
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
