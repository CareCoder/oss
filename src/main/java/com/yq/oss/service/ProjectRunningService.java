package com.yq.oss.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.yq.oss.entity.vo.ProjectSourceDO;

public interface ProjectRunningService {
    void run(ProjectSourceDO projectSourceDO);

    void setJobJenkinsServer(Long id, JenkinsServer jenkinsServer);

    void jenkinsBuildComplete(Long id);
}
