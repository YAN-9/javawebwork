package com.gzu.chuanxinrecruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.utils.Result;
import com.gzu.chuanxinrecruitment.entity.InterviewSchedule;
import com.gzu.chuanxinrecruitment.service.InterviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewScheduleController {

    @Autowired
    private InterviewScheduleService scheduleService;

    @GetMapping("/page")
    public Result<IPage<InterviewSchedule>> getSchedulePage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.OK(scheduleService.getSchedulePage(currentPage, pageSize));
    }

    @GetMapping("/{id}")
    public Result<InterviewSchedule> getScheduleById(@PathVariable Integer id) {
        InterviewSchedule schedule = scheduleService.getScheduleById(id);
        return schedule != null ? Result.OK(schedule) : Result.error("面试安排不存在");
    }

    @GetMapping("/application/{applicationId}")
    public Result<List<InterviewSchedule>> getSchedulesByApplicationId(@PathVariable Integer applicationId) {
        return Result.OK(scheduleService.getSchedulesByApplicationId(applicationId));
    }

    @GetMapping("/date/{date}")
    public Result<List<InterviewSchedule>> getSchedulesByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return Result.OK(scheduleService.getSchedulesByDate(date));
    }

    @PostMapping
    public Result<Boolean> addSchedule(@RequestBody InterviewSchedule schedule) {

        if (schedule.getApplicationId() == null || 
            schedule.getInterviewTime() == null || 
            schedule.getPlatform() == null) {
            return Result.error("缺少必要的面试信息");
        }

        if (schedule.getInterviewTime().before(new Date())) {
            return Result.error("面试时间不能早于当前时间");
        }

        schedule.setCreatedAt(new Date());
        schedule.setUpdatedAt(new Date());
        schedule.setStatus("待面试");

        try {
            List<InterviewSchedule> existingSchedules = scheduleService.getSchedulesByApplicationId(schedule.getApplicationId());
            boolean hasActiveInterview = existingSchedules.stream()
                .anyMatch(s -> "待面试".equals(s.getStatus()));
            
            if (hasActiveInterview) {
                return Result.error("该申请已有待面试的安排");
            }

            return scheduleService.addSchedule(schedule) ?
                    Result.OK("面试安排创建成功") : Result.error("面试安排创建失败");
        } catch (Exception e) {
            return Result.error("创建面试安排失败: " + e.getMessage());
        }
    }

    @PutMapping
    public Result<Boolean> updateSchedule(@RequestBody InterviewSchedule schedule) {
        schedule.setUpdatedAt(new Date());
        return scheduleService.updateSchedule(schedule) ?
                Result.OK("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSchedule(@PathVariable Integer id) {
        return scheduleService.deleteSchedule(id) ?
                Result.OK("删除成功") : Result.error("删除失败");
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        InterviewSchedule schedule = new InterviewSchedule();
        schedule.setId(id);
        schedule.setStatus(status);
        schedule.setUpdatedAt(new Date());
        return scheduleService.updateSchedule(schedule) ? 
               Result.OK("状态更新成功") : Result.error("状态更新失败");
    }
} 