package com.gzu.chuanxinrecruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.utils.Result;
import com.gzu.chuanxinrecruitment.entity.Application;
import com.gzu.chuanxinrecruitment.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/page")
    public Result<IPage<Application>> getApplicationPage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.OK(applicationService.getApplicationPage(currentPage, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Application> getApplicationById(@PathVariable Integer id) {
        Application application = applicationService.getApplicationById(id);
        return application != null ? Result.OK(application) : Result.error("申请不存在");
    }

    @GetMapping("/resume/{resumeId}")
    public Result<List<Application>> getApplicationsByResumeId(@PathVariable Integer resumeId) {
        return Result.OK(applicationService.getApplicationsByResumeId(resumeId));
    }

    @GetMapping("/job/{jobId}")
    public Result<List<Application>> getApplicationsByJobId(@PathVariable Integer jobId) {
        return Result.OK(applicationService.getApplicationsByJobId(jobId));
    }

    @PostMapping
    public Result<Boolean> addApplication(@RequestBody Application application) {
        application.setCreatedAt(new Date());
        application.setUpdatedAt(new Date());
        application.setStatus("已投递"); // 设置初始状态
        return applicationService.addApplication(application) ? 
               Result.OK("申请提交成功") : Result.error("申请提交失败");
    }

    @PutMapping
    public Result<Boolean> updateApplication(@RequestBody Application application) {
        return applicationService.updateApplication(application) ? 
               Result.OK("更新成功") : Result.error("更新失败");
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateApplicationStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return applicationService.updateApplicationStatus(id, status) ? 
               Result.OK("状态更新成功") : Result.error("状态更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteApplication(@PathVariable Integer id) {
        return applicationService.deleteApplication(id) ? 
               Result.OK("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/status/{status}")
    public Result<List<Application>> getApplicationsByStatus(@PathVariable String status) {
        return Result.OK(applicationService.getApplicationsByStatus(status));
    }
} 