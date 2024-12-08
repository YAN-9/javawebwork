package com.gzu.chuanxinrecruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzu.chuanxinrecruitment.entity.Resume;
import com.gzu.chuanxinrecruitment.mapper.ResumeMapper;
import com.gzu.chuanxinrecruitment.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Override
    public IPage<Resume> getResumePage(Integer currentPage, Integer pageSize) {
        Page<Resume> page = new Page<>(currentPage, pageSize);
        return resumeMapper.selectPage(page, null);
    }

    @Override
    public Resume getResumeById(Integer id) {
        return resumeMapper.selectById(id);
    }

    @Override
    public Resume getResumeByUserId(Integer userId) {
        QueryWrapper<Resume> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return resumeMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean addResume(Resume resume) {
        return resumeMapper.insert(resume) > 0;
    }

    @Override
    public boolean updateResume(Resume resume) {
        return resumeMapper.updateById(resume) > 0;
    }

    @Override
    public boolean deleteResume(Integer id) {
        return resumeMapper.deleteById(id) > 0;
    }

} 