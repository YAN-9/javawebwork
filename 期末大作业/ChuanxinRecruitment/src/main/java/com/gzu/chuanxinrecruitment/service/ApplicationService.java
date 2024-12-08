package com.gzu.chuanxinrecruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.Application;
import java.util.List;
import java.util.Map;

public interface ApplicationService {
    IPage<Application> getApplicationPage(Integer currentPage, Integer pageSize);
    Application getApplicationById(Integer id);
    List<Application> getApplicationsByResumeId(Integer resumeId);
    List<Application> getApplicationsByJobId(Integer jobId);
    boolean addApplication(Application application);
    boolean updateApplication(Application application);
    boolean deleteApplication(Integer id);
    List<Application> getApplicationsByStatus(String status);
    boolean updateApplicationStatus(Integer id, String status);
    List<Map<String, Object>> getApplicationStatusStats();
} 