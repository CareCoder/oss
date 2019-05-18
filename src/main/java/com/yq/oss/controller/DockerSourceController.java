package com.yq.oss.controller;

import com.yq.oss.common.PageResponse;
import com.yq.oss.entity.dto.DockerSource;
import com.yq.oss.service.DockerSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/docker_source")
@Slf4j
public class DockerSourceController {
    @Autowired
    private DockerSourceService dockerSourceService;

    @GetMapping("/list.html")
    public String listHtml() {
        return "/docker_source_list";
    }

    @GetMapping("/addOrEdit.html")
    public String addOrEditHtml(Long id, Model model) {
        if (id != null) {
            DockerSource dockerSource = dockerSourceService.findById(id);
            model.addAttribute("dockerSource", dockerSource);
        }
        return "/docker_source_addOrEdit";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<DockerSource> list() {
        List<DockerSource> list = dockerSourceService.list();
        return PageResponse.build(list.size(), list);
    }

    @PostMapping("addOrEdit")
    @ResponseBody
    public void addOrEdit(DockerSource dockerSource) {
        dockerSourceService.addOrEdit(dockerSource);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        dockerSourceService.delete(id);
    }
}
