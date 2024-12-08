package com.gzu.chuanxinrecruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzu.chuanxinrecruitment.entity.CompanyReview;
import com.gzu.chuanxinrecruitment.mapper.CompanyReviewMapper;
import com.gzu.chuanxinrecruitment.service.CompanyReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CompanyReviewServiceImpl extends ServiceImpl<CompanyReviewMapper, CompanyReview> 
        implements CompanyReviewService {

    @Autowired
    private CompanyReviewMapper reviewMapper;

    @Override
    public boolean addReview(CompanyReview review) {
        review.setCreatedAt(new Date());
        return save(review);
    }

    @Override
    public CompanyReview getReviewById(Integer id) {
        return getById(id);
    }

    @Override
    public boolean updateReview(CompanyReview review) {
        return updateById(review);
    }

    @Override
    public boolean deleteReview(Integer id) {
        return removeById(id);
    }

    @Override
    public IPage<CompanyReview> getReviewPage(Integer currentPage, Integer pageSize) {
        Page<CompanyReview> page = new Page<>(currentPage, pageSize);
        return page(page, new QueryWrapper<CompanyReview>().orderByDesc("created_at"));
    }

    @Override
    public List<CompanyReview> getReviewsByCompanyId(Integer companyId) {
        QueryWrapper<CompanyReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", companyId)
                   .orderByDesc("created_at");
        return list(queryWrapper);
    }

    @Override
    public List<CompanyReview> getReviewsByUserId(Integer userId) {
        QueryWrapper<CompanyReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("created_at");
        return list(queryWrapper);
    }

    @Override
    public List<CompanyReview> getRecentReviews(String companyName, int limit) {
        return reviewMapper.getRecentReviews(companyName, limit);
    }

    @Override
    public Double getAverageRating(String companyName) {
        return reviewMapper.getAverageRating(companyName);
    }

    @Override
    public List<Map<String, Object>> getCompanyRatingSummary() {
        return reviewMapper.getCompanyRatingSummary();
    }

    @Override
    public List<Map<String, Object>> getRatingDistribution(String companyName) {
        return reviewMapper.getRatingDistribution(companyName);
    }
} 