package com.gzu.chuanxinrecruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.Job;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

public interface JobService {
    IPage<Job> getJobPage(Integer currentPage, Integer pageSize, String keyword,
                          String location, String salary, String time, String sort);
    List<Job> getRecommendedJobs(Integer userId);
    Job getJobById(Integer id);
    List<Job> getJobsByHrId(Integer hrId);
    boolean addJob(Job job);
    boolean updateJob(Job job);
    boolean deleteJob(Integer id);
    List<Map<String, Object>> getJobTrendData(LocalDateTime startDate);
}