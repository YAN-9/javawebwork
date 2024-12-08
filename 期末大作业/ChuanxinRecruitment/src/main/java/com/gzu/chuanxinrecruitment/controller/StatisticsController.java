package com.gzu.chuanxinrecruitment.controller;

import com.gzu.chuanxinrecruitment.utils.Result;
import com.gzu.chuanxinrecruitment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private InterviewScheduleService interviewService;

    @GetMapping("/overview")
    public Result<?> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取各项统计数据
        stats.put("totalUsers", userService.getAllUser().size());
        stats.put("totalJobs", jobService.getJobPage(1, Integer.MAX_VALUE, null, null, null, null, null).getTotal());
        stats.put("totalApplications", applicationService.getApplicationPage(1, Integer.MAX_VALUE).getTotal());
        stats.put("totalInterviews", interviewService.getSchedulePage(1, Integer.MAX_VALUE).getTotal());
        
        return Result.OK(stats);
    }

    @GetMapping("/user-growth")
    public Result<?> getUserGrowthData(@RequestParam Integer days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Map<String, Object>> userGrowth = userService.getUserGrowthData(startDate);
        
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        
        for (Map<String, Object> data : userGrowth) {
            dates.add(data.get("date").toString());
            counts.add(((Number) data.get("count")).intValue());
        }
        
        result.put("dates", dates);
        result.put("counts", counts);
        
        return Result.OK(result);
    }

    @GetMapping("/job-trend")
    public Result<?> getJobTrendData(@RequestParam Integer days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Map<String, Object>> jobTrend = jobService.getJobTrendData(startDate);
        
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        
        for (Map<String, Object> data : jobTrend) {
            dates.add(data.get("date").toString());
            counts.add(((Number) data.get("count")).intValue());
        }
        
        result.put("dates", dates);
        result.put("counts", counts);
        
        return Result.OK(result);
    }

    @GetMapping("/application-status")
    public Result<?> getApplicationStatusData() {
        List<Map<String, Object>> statusData = applicationService.getApplicationStatusStats();
        return Result.OK(statusData);
    }
} 