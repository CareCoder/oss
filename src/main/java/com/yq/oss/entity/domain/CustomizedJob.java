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

    /**
     * 这个任务所对应的容器id
     * 理论上来说不需要这个id也可以完成业务需求
     * 不过需要重新获取docker上面的container和images的快照
     * 这样可以增加性能
     */
    private String containerId;

    public static CustomizedJob buildDefault(ProjectSourceDO projectSourceDO, JobStatus status) {
        CustomizedJob job = new CustomizedJob();
        job.setProjectSourceDO(projectSourceDO);
        job.setJenkinsJobStatus(status);
        return job;
    }
}
