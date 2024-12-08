package com.gzu.chuanxinrecruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzu.chuanxinrecruitment.utils.Result;
import com.gzu.chuanxinrecruitment.entity.Resume;
import com.gzu.chuanxinrecruitment.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/page")
    public Result<IPage<Resume>> getResumePage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.OK(resumeService.getResumePage(currentPage, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Resume> getResumeById(@PathVariable Integer id) {
        Resume resume = resumeService.getResumeById(id);
        return resume != null ? Result.OK(resume) : Result.error("简历不存在");
    }

    @GetMapping("/user/{userId}")
    public Result<Resume> getResumeByUserId(@PathVariable Integer userId) {
        Resume resume = resumeService.getResumeByUserId(userId);
        return resume != null ? Result.OK(resume) : Result.error("该用户暂无简历");
    }

    @PostMapping
    public Result<Boolean> addResume(@RequestBody Resume resume) {
        resume.setCreatedAt(new Date());
        resume.setUpdatedAt(new Date());
        return resumeService.addResume(resume) ? 
               Result.OK("简历创建成功") : Result.error("简历创建失败");
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateResume(@RequestBody Resume resume) {
        resume.setUpdatedAt(new Date());
        return resumeService.updateResume(resume) ? 
               Result.OK("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteResume(@PathVariable Integer id) {
        return resumeService.deleteResume(id) ? 
               Result.OK("删除成功") : Result.error("删除失败");
    }

}