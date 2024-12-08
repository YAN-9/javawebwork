package com.gzu.chuanxinrecruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzu.chuanxinrecruitment.entity.Application;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {
    List<Map<String, Object>> getApplicationStatusStats();
} 