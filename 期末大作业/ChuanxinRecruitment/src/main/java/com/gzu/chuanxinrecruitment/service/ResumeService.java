package com.gzu.chuanxinrecruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.entity.Resume;
import java.util.List;

public interface ResumeService {
    IPage<Resume> getResumePage(Integer currentPage, Integer pageSize);
    Resume getResumeById(Integer id);
    Resume getResumeByUserId(Integer userId);
    boolean addResume(Resume resume);
    boolean updateResume(Resume resume);
    boolean deleteResume(Integer id);
} 