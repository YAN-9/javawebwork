package com.gzu.chuanxinrecruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.utils.Result;
import com.gzu.chuanxinrecruitment.entity.CompanyReview;
import com.gzu.chuanxinrecruitment.service.CompanyReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class CompanyReviewController {

    @Autowired
    private CompanyReviewService reviewService;

    @GetMapping("/page")
    public Result<IPage<CompanyReview>> getReviewPage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.OK(reviewService.getReviewPage(currentPage, pageSize));
    }

    @GetMapping("/{id}")
    public Result<CompanyReview> getReviewById(@PathVariable Integer id) {
        CompanyReview review = reviewService.getReviewById(id);
        return review != null ? Result.OK(review) : Result.error("评论不存在");
    }

    @GetMapping("/company/{companyId}")
    public Result<List<CompanyReview>> getReviewsByCompanyId(@PathVariable Integer companyId) {
        return Result.OK(reviewService.getReviewsByCompanyId(companyId));
    }

    @GetMapping("/user/{userId}")
    public Result<List<CompanyReview>> getReviewsByUserId(@PathVariable Integer userId) {
        return Result.OK(reviewService.getReviewsByUserId(userId));
    }

    @PostMapping
    public Result<Boolean> addReview(@Valid @RequestBody CompanyReview review) {
        return reviewService.addReview(review) ? 
               Result.OK("评论发布成功") : Result.error("评论发布失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteReview(@PathVariable Integer id) {
        return reviewService.deleteReview(id) ? 
               Result.OK("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/rating/{companyName}")
    public Result<Double> getAverageRating(@PathVariable String companyName) {
        return Result.OK(reviewService.getAverageRating(companyName));
    }

    @GetMapping("/stats/summary")
    public Result<List<Map<String, Object>>> getCompanyRatingSummary() {
        return Result.OK(reviewService.getCompanyRatingSummary());
    }

    @GetMapping("/stats/distribution/{companyName}")
    public Result<List<Map<String, Object>>> getRatingDistribution(@PathVariable String companyName) {
        return Result.OK(reviewService.getRatingDistribution(companyName));
    }
} 