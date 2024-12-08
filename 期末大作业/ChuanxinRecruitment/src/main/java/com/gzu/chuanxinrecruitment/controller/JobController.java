package com.gzu.chuanxinrecruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.utils.Result;
import com.gzu.chuanxinrecruitment.entity.Job;
import com.gzu.chuanxinrecruitment.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // 分页查询所有职位
    @GetMapping("/page")
    public Result<IPage<Job>> getJobPage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String salary,
            @RequestParam(required = false) String time,
            @RequestParam(defaultValue = "latest") String sort) {
        return Result.OK(jobService.getJobPage(currentPage, pageSize, keyword, location, salary, time, sort));
    }

    // 获取推荐职位
    @GetMapping("/recommended")
    public Result<List<Job>> getRecommendedJobs(@RequestParam Integer userId) {
        return Result.OK(jobService.getRecommendedJobs(userId));
    }

    // 根据ID获取职位详情
    @GetMapping("/{id}")
    public Result<Job> getJobById(@PathVariable Integer id) {
        Job job = jobService.getJobById(id);
        return job != null ? Result.OK(job) : Result.error("职位不存在");
    }

    // 获取HR发布的职位
    @GetMapping("/hr/{hrId}")
    public Result<List<Job>> getJobsByHrId(@PathVariable Integer hrId) {
        return Result.OK(jobService.getJobsByHrId(hrId));
    }

    // 添加职位
    @PostMapping
    public Result<Boolean> addJob(@RequestBody Job job) {
        job.setCreatedAt(new Date());
        job.setUpdatedAt(new Date());
        return jobService.addJob(job) ? Result.OK("添加成功") : Result.error("添加失败");
    }

    // 更新职位
    @PutMapping
    public Result<Boolean> updateJob(@RequestBody Job job) {
        job.setUpdatedAt(new Date());
        return jobService.updateJob(job) ? Result.OK("更新成功") : Result.error("更新失败");
    }

    // 删除职位
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteJob(@PathVariable Integer id) {
        return jobService.deleteJob(id) ? Result.OK("删除成功") : Result.error("删除失败");
    }
}