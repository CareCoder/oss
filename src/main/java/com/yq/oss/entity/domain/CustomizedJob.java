package com.yq.oss.entity.domain;

import com.github.dockerjava.api.DockerClient;
import com.offbytwo.jenkins.JenkinsServer;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.enums.JobStatus;
import lombok.Data;

@Data
public class CustomizedJob {
    private ProjectSourceDO projectSourceDO;

    private JobStatus jenkinsJobStatus;

    private JenkinsServer jenkinsServer;

    private DockerClient dockerClient;

    private String containerId;

    public static CustomizedJob buildDefault(ProjectSourceDO projectSourceDO, JobStatus status) {
        CustomizedJob job = new CustomizedJob();
        job.setProjectSourceDO(projectSourceDO);
        job.setJenkinsJobStatus(status);
        return job;
    }
}
