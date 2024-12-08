package com.gzu.chuanxinrecruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 分页查询
    List<User> selectPageVo(IPage<User> page);

    List<Map<String, Object>> getUserGrowthData(@Param("startDate") LocalDateTime startDate);

}
