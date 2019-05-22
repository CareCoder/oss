package com.yq.oss.service.impl;

import com.yq.oss.entity.dto.JenkinsSource;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.service.ProjectRunningService;
import com.yq.oss.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ProjectRunningServiceImpl implements ProjectRunningService {
    @Override
    public void run(ProjectSourceDO projectSourceDO) {
        JenkinsSource jenkinsSource = projectSourceDO.getJenkinsSource();
        String jenkinsTiggerUrl = buildJenkinsTiggerUrl(jenkinsSource.getHost(), jenkinsSource.getProjectName());
        Map<String, String> params = buildJenkinsParams(projectSourceDO);
        Map<String, String> headers = buildJenkinsHeader(projectSourceDO.getJenkinsSource());
        try {
            HttpUtils.httpGet(jenkinsTiggerUrl, params, headers);
        } catch (Exception e) {
            log.info("ProjectRunningServiceImpl run error", e);
        }
    }

    private Map<String, String> buildJenkinsHeader(JenkinsSource jenkinsSource) {
        Map<String, String> headers = new HashMap<>();
        String str = jenkinsSource.getUsername() + ":" + jenkinsSource.getPassword();
        headers.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(str.getBytes()));
        return headers;
    }

    private Map<String,String> buildJenkinsParams(ProjectSourceDO projectSourceDO) {
        Map<String, String> params = new HashMap<>();
        params.put("token", projectSourceDO.getJenkinsSource().getToken());
        params.put("version", projectSourceDO.getVersion());
        params.put("address", projectSourceDO.getGitAddress());
        params.put("image", projectSourceDO.getImageName() + ":" + projectSourceDO.getImageTag());
        return params;
    }

    private String buildJenkinsTiggerUrl(String jenkinsHost, String jenkinsProjectName) {
        if (!jenkinsHost.startsWith("http")) {
            jenkinsHost = "http://" + jenkinsHost;
        }
        return jenkinsHost +
                "/job/" +
                jenkinsProjectName +
                "/buildWithParameters";
    }
}
