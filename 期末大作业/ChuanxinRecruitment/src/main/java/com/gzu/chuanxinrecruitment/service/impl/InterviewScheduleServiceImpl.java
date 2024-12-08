package com.gzu.chuanxinrecruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzu.chuanxinrecruitment.entity.InterviewSchedule;
import com.gzu.chuanxinrecruitment.mapper.InterviewScheduleMapper;
import com.gzu.chuanxinrecruitment.service.InterviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    @Autowired
    private InterviewScheduleMapper scheduleMapper;

    @Override
    public IPage<InterviewSchedule> getSchedulePage(Integer currentPage, Integer pageSize) {
        Page<InterviewSchedule> page = new Page<>(currentPage, pageSize);
        return scheduleMapper.selectPage(page, null);
    }

    @Override
    public InterviewSchedule getScheduleById(Integer id) {
        return scheduleMapper.selectById(id);
    }

    @Override
    public List<InterviewSchedule> getSchedulesByApplicationId(Integer applicationId) {
        QueryWrapper<InterviewSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("application_id", applicationId)
                .orderByDesc("interview_time");
        return scheduleMapper.selectList(queryWrapper);
    }

    @Override
    public List<InterviewSchedule> getSchedulesByDate(Date date) {
        QueryWrapper<InterviewSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interview_time", date)
                .orderByAsc("interview_time");
        return scheduleMapper.selectList(queryWrapper);
    }

    @Override
    public boolean addSchedule(InterviewSchedule schedule) {
        if (schedule.getInterviewTime().before(new Date())) {
            throw new IllegalArgumentException("面试时间不能早于当前时间");
        }

        QueryWrapper<InterviewSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("application_id", schedule.getApplicationId())
                   .eq("status", "待面试");
        
        if (scheduleMapper.selectCount(queryWrapper) > 0) {
            throw new IllegalArgumentException("该申请已有待面试的安排");
        }

        return scheduleMapper.insert(schedule) > 0;
    }

    @Override
    public boolean updateSchedule(InterviewSchedule schedule) {
        if (schedule.getInterviewTime() == null || schedule.getPlatform() == null) {
            InterviewSchedule original = scheduleMapper.selectById(schedule.getId());
            if (original == null) {
                return false;
            }
            schedule.setInterviewTime(original.getInterviewTime());
            schedule.setPlatform(original.getPlatform());
            schedule.setApplicationId(original.getApplicationId());
        }
        return scheduleMapper.updateById(schedule) > 0;
    }

    @Override
    public boolean deleteSchedule(Integer id) {
        return scheduleMapper.deleteById(id) > 0;
    }

    @Override
    public List<InterviewSchedule> getSchedulesByStatus(String status) {
        QueryWrapper<InterviewSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status)
                .orderByDesc("interview_time");
        return scheduleMapper.selectList(queryWrapper);
    }
} 