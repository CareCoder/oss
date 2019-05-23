package com.yq.oss.controller;

import com.yq.oss.common.PageResponse;
import com.yq.oss.entity.dto.ProjectSource;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.service.ProjectSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project_source")
public class ProjectSourceController {
    @Autowired
    private ProjectSourceService projectSourceService;

    @GetMapping("/list.html")
    public String listHtml() {
        return "/project_source_list";
    }

    @GetMapping("/addOrEdit.html")
    public String addOrEditHtml(Long id, Model model) {
        if (id != null) {
            ProjectSource projectSource = projectSourceService.findById(id);
            model.addAttribute("projectSource", projectSource);
        }
        return "/project_source_addOrEdit";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<ProjectSourceDO> list() {
        List<ProjectSourceDO> list = projectSourceService.list();
        return PageResponse.build(list.size(), list);
    }

    @GetMapping("/list/detail")
    @ResponseBody
    public PageResponse<ProjectSourceDO> listDetail() {
        List<ProjectSourceDO> list = projectSourceService.listDetail();
        return PageResponse.build(list.size(), list);
    }

    @PostMapping("/addOrEdit")
    @ResponseBody
    public void addOrEdit(ProjectSource projectSource) {
        projectSourceService.addOrEdit(projectSource);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        projectSourceService.delete(id);
    }
}
