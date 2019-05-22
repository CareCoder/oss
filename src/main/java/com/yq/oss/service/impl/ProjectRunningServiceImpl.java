package com.yq.oss.service.impl;

import com.offbytwo.jenkins.JenkinsServer;
import com.yq.oss.entity.domain.CustomizedJob;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.enums.JenkinsJobStatus;
import com.yq.oss.service.ProjectRunningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ProjectRunningServiceImpl implements ProjectRunningService {
    public static Map<Long, CustomizedJob> CUSTOMIZED_JENKINS_JOBS = new ConcurrentHashMap<>();
    @Override
    public void run(ProjectSourceDO projectSourceDO) {
        CUSTOMIZED_JENKINS_JOBS.put(projectSourceDO.getId(), CustomizedJob.buildDefault(projectSourceDO, JenkinsJobStatus.NOT_RUNNING));
    }

    @Override
    public void setJobJenkinsServer(Long id, JenkinsServer jenkinsServer) {
        CustomizedJob job = CUSTOMIZED_JENKINS_JOBS.get(id);
        job.setJenkinsServer(jenkinsServer);
        job.setJenkinsJobStatus(JenkinsJobStatus.JENKINS_RUNNING);
    }

    @Override
    public void jenkinsBuildComplete(Long id) {
        CustomizedJob job = CUSTOMIZED_JENKINS_JOBS.get(id);
        job.setJenkinsJobStatus(JenkinsJobStatus.JENKINS_COMPLETE);
    }
}
