package com.yq.oss.service;

import com.github.dockerjava.api.DockerClient;
import com.offbytwo.jenkins.JenkinsServer;
import com.yq.oss.entity.vo.CustomizedJobVO;
import com.yq.oss.entity.vo.ProjectSourceDO;

import java.util.List;

public interface ProjectRunningService {
    void run(ProjectSourceDO projectSourceDO);

    void setJobJenkinsServer(Long id, JenkinsServer jenkinsServer);

    void setDockerClientInfo(Long id, DockerClient dockerClient, String containerId);

    void jenkinsBuildComplete(Long id);

    void dockerContainerStartComplete(Long id);

    List<CustomizedJobVO> listJobStatus();
}
