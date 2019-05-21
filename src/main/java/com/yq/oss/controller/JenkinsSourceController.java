package com.yq.oss.controller;

import com.yq.oss.common.PageResponse;
import com.yq.oss.entity.dto.JenkinsSource;
import com.yq.oss.service.JenkinsSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jenkins_source")
@Slf4j
public class JenkinsSourceController {
    @Autowired
    private JenkinsSourceService jenkinsSourceService;

    @GetMapping("/list.html")
    public String listHtml() {
        return "/jenkins_source_list";
    }

    @GetMapping("/addOrEdit.html")
    public String addOrEditHtml(Long id, Model model) {
        if (id != null) {
            JenkinsSource jenkinsSource = jenkinsSourceService.findById(id);
            model.addAttribute("jenkinsSource", jenkinsSource);
        }
        return "/jenkins_source_addOrEdit";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<JenkinsSource> list() {
        List<JenkinsSource> list = jenkinsSourceService.list();
        return PageResponse.build(list.size(), list);
    }

    @PostMapping("addOrEdit")
    @ResponseBody
    public void addOrEdit(JenkinsSource jenkinsSource) {
        jenkinsSourceService.addOrEdit(jenkinsSource);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        jenkinsSourceService.delete(id);
    }
}
