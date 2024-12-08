package com.gzu.chuanxinrecruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzu.chuanxinrecruitment.entity.CompanyReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CompanyReviewMapper extends BaseMapper<CompanyReview> {

    Double getAverageRating(String companyName);

    List<Map<String, Object>> getCompanyRatingSummary();

    List<Map<String, Object>> getRatingDistribution(String companyName);

    List<CompanyReview> getRecentReviews(@Param("companyName") String companyName, @Param("limit") int limit);
} 