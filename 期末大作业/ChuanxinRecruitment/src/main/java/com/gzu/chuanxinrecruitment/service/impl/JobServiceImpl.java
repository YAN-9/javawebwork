package com.gzu.chuanxinrecruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzu.chuanxinrecruitment.entity.Job;
import com.gzu.chuanxinrecruitment.entity.Resume;
import com.gzu.chuanxinrecruitment.entity.User;
import com.gzu.chuanxinrecruitment.mapper.JobMapper;
import com.gzu.chuanxinrecruitment.mapper.ResumeMapper;
import com.gzu.chuanxinrecruitment.mapper.UserMapper;
import com.gzu.chuanxinrecruitment.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<Job> getJobPage(Integer currentPage, Integer pageSize, String keyword,
                                 String location, String salary, String time, String sort) {
        Page<Job> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();

        // 添加搜索条件
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("title", keyword)
                    .or()
                    .like("description", keyword)
                    .or()
                    .like("requirements", keyword);
        }

        if (StringUtils.hasText(location)) {
            queryWrapper.like("location", location);
        }

        if (StringUtils.hasText(salary)) {
            queryWrapper.like("salary_range", salary);
        }

        // 添加时间筛选
        if ("today".equals(time)) {
            queryWrapper.ge("created_at", LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        } else if ("week".equals(time)) {
            queryWrapper.ge("created_at", LocalDateTime.now().minusWeeks(1));
        } else if ("month".equals(time)) {
            queryWrapper.ge("created_at", LocalDateTime.now().minusMonths(1));
        }

        // 添加排序
        if ("latest".equals(sort)) {
            queryWrapper.orderByDesc("created_at");
        } else if ("salary".equals(sort)) {
            queryWrapper.orderByDesc("salary_range");
        }

        return jobMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Job> getRecommendedJobs(Integer userId) {
        // 1. 获取用户简历
        Resume resume = resumeMapper.selectOne(
            new QueryWrapper<Resume>().eq("user_id", userId)
        );
        
        // 2. 获取用户信息
        User user = userMapper.selectById(userId);
        
        // 3. 构建推荐查询条件
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        
        if (resume != null) {
            // 从简历中提取关键信息进行匹配
            String skills = resume.getSkills();
            String education = resume.getEducation();
            String experience = resume.getExperience();
            
            if (StringUtils.hasText(skills)) {
                // 使用 OR 条件匹配职位要求中的技能
                queryWrapper.like("requirements", skills)
                        .or()
                        .like("description", skills);
            }
            
            if (StringUtils.hasText(education)) {
                queryWrapper.like("requirements", education);
            }
            
            if (StringUtils.hasText(experience)) {
                // 根据工作经验匹配相关职位
                queryWrapper.like("requirements", experience)
                        .or()
                        .like("description", experience);
            }
        }
        
        // 4. 添加时间限制和排序
        queryWrapper.ge("created_at", LocalDateTime.now().minusMonths(1)) // 最近一个月的职位
                .orderByDesc("created_at");
        
        // 5. 限制返回数量
        queryWrapper.last("LIMIT 6");
        
        // 6. 如果没有匹配到足够的职位，补充最新职位
        List<Job> recommendedJobs = jobMapper.selectList(queryWrapper);
        if (recommendedJobs.size() < 6) {
            QueryWrapper<Job> latestJobsWrapper = new QueryWrapper<>();
            latestJobsWrapper.orderByDesc("created_at")
                    .last("LIMIT " + (6 - recommendedJobs.size()));
            List<Job> latestJobs = jobMapper.selectList(latestJobsWrapper);
            recommendedJobs.addAll(latestJobs);
        }
        
        return recommendedJobs;
    }

    @Override
    public Job getJobById(Integer id) {
        return jobMapper.selectById(id);
    }

    @Override
    public List<Job> getJobsByHrId(Integer hrId) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hr_id", hrId)
                .orderByDesc("created_at");
        return jobMapper.selectList(queryWrapper);
    }

    @Override
    public boolean addJob(Job job) {
        return jobMapper.insert(job) > 0;
    }

    @Override
    public boolean updateJob(Job job) {
        return jobMapper.updateById(job) > 0;
    }

    @Override
    public boolean deleteJob(Integer id) {
        return jobMapper.deleteById(id) > 0;
    }

    @Override
    public List<Map<String, Object>> getJobTrendData(LocalDateTime startDate) {
        return jobMapper.getJobTrendData(startDate);
    }
}