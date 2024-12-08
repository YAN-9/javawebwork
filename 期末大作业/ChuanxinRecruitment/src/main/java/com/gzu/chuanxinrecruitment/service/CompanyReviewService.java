package com.gzu.chuanxinrecruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.CompanyReview;

import java.util.List;
import java.util.Map;

public interface CompanyReviewService {
    
    // 基础CRUD操作
    boolean addReview(CompanyReview review);
    CompanyReview getReviewById(Integer id);
    boolean updateReview(CompanyReview review);
    boolean deleteReview(Integer id);
    IPage<CompanyReview> getReviewPage(Integer currentPage, Integer pageSize);
    
    // 查询方法
    List<CompanyReview> getReviewsByCompanyId(Integer companyId);
    List<CompanyReview> getReviewsByUserId(Integer userId);
    List<CompanyReview> getRecentReviews(String companyName, int limit);
    
    // 统计分析方法
    Double getAverageRating(String companyName);
    List<Map<String, Object>> getCompanyRatingSummary();
    List<Map<String, Object>> getRatingDistribution(String companyName);
} 