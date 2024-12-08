package com.gzu.chuanxinrecruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzu.chuanxinrecruitment.entity.Application;
import com.gzu.chuanxinrecruitment.mapper.ApplicationMapper;
import com.gzu.chuanxinrecruitment.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public IPage<Application> getApplicationPage(Integer currentPage, Integer pageSize) {
        Page<Application> page = new Page<>(currentPage, pageSize);
        return applicationMapper.selectPage(page, null);
    }

    @Override
    public Application getApplicationById(Integer id) {
        return applicationMapper.selectById(id);
    }

    @Override
    public List<Application> getApplicationsByResumeId(Integer resumeId) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resume_id", resumeId)
                .orderByDesc("created_at");
        return applicationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Application> getApplicationsByJobId(Integer jobId) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id", jobId)
                .orderByDesc("created_at");
        return applicationMapper.selectList(queryWrapper);
    }

    @Override
    public boolean addApplication(Application application) {
        return applicationMapper.insert(application) > 0;
    }

    @Override
    public boolean updateApplication(Application application) {
        application.setUpdatedAt(new Date());
        return applicationMapper.updateById(application) > 0;
    }

    @Override
    public boolean deleteApplication(Integer id) {
        return applicationMapper.deleteById(id) > 0;
    }

    @Override
    public List<Application> getApplicationsByStatus(String status) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status)
                .orderByDesc("created_at");
        return applicationMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateApplicationStatus(Integer id, String status) {
        Application application = new Application();
        application.setId(id);
        application.setStatus(status);
        application.setUpdatedAt(new Date());
        return applicationMapper.updateById(application) > 0;
    }

    @Override
    public List<Map<String, Object>> getApplicationStatusStats() {
        return applicationMapper.getApplicationStatusStats();
    }
} 