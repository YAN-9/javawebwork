package com.gzu.chuanxinrecruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.InterviewSchedule;
import java.util.List;
import java.util.Date;

public interface InterviewScheduleService {
    IPage<InterviewSchedule> getSchedulePage(Integer currentPage, Integer pageSize);
    InterviewSchedule getScheduleById(Integer id);
    List<InterviewSchedule> getSchedulesByApplicationId(Integer applicationId);
    List<InterviewSchedule> getSchedulesByDate(Date date);
    boolean addSchedule(InterviewSchedule schedule);
    boolean updateSchedule(InterviewSchedule schedule);
    boolean deleteSchedule(Integer id);
    List<InterviewSchedule> getSchedulesByStatus(String status);
} 