package com.yq.oss.controller;

import com.google.common.base.Strings;
import com.yq.oss.entity.vo.CustomizedJobVO;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.service.ProjectRunningService;
import com.yq.oss.service.ProjectSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/project_running")
public class ProjectRunningController {
    @Autowired
    private ProjectSourceService projectSourceService;
    @Autowired
    private ProjectRunningService projectRunningService;

    @GetMapping("/list.html")
    public String list() {
        return "/project_running_list";
    }

    @PostMapping("/run")
    @ResponseBody
    public void run(Long id, String version, String imageTag) {
        ProjectSourceDO projectSourceDO = projectSourceService.findDOById(id);
        if (!Strings.isNullOrEmpty(version)) {
            projectSourceDO.setVersion(version);
        }
        if (!Strings.isNullOrEmpty(imageTag)) {
            projectSourceDO.setImageTag(imageTag);
        }
        projectRunningService.run(projectSourceDO);
    }

    @GetMapping("/listJobStatus")
    @ResponseBody
    public List<CustomizedJobVO> listJobStatus() {
        return projectRunningService.listJobStatus();
    }

}

