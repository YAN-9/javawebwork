package com.gzu.chuanxinrecruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzu.chuanxinrecruitment.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface JobMapper extends BaseMapper<Job> {
    List<Map<String, Object>> getJobTrendData(@Param("startDate") LocalDateTime startDate);
} 