package com.yq.oss.entity.domain;

import com.offbytwo.jenkins.JenkinsServer;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.enums.JenkinsJobStatus;
import lombok.Data;

@Data
public class CustomizedJob {
    private ProjectSourceDO projectSourceDO;

    private JenkinsJobStatus jenkinsJobStatus;

    private JenkinsServer jenkinsServer;

    public static CustomizedJob buildDefault(ProjectSourceDO projectSourceDO, JenkinsJobStatus status) {
        CustomizedJob job = new CustomizedJob();
        job.setProjectSourceDO(projectSourceDO);
        job.setJenkinsJobStatus(status);
        return job;
    }
}
