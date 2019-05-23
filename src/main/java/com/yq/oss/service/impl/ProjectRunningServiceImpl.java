package com.yq.oss.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.offbytwo.jenkins.JenkinsServer;
import com.yq.oss.entity.domain.CustomizedJob;
import com.yq.oss.entity.vo.CustomizedJobVO;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.enums.JobStatus;
import com.yq.oss.service.ProjectRunningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectRunningServiceImpl implements ProjectRunningService {
    public static Map<Long, CustomizedJob> CUSTOMIZED_JENKINS_JOBS = new ConcurrentHashMap<>();
    @Override
    public void run(ProjectSourceDO projectSourceDO) {
        CUSTOMIZED_JENKINS_JOBS.putIfAbsent(projectSourceDO.getId(), CustomizedJob.buildDefault(projectSourceDO, JobStatus.NOT_RUNNING));
    }

    @Override
    public void setJobJenkinsServer(Long id, JenkinsServer jenkinsServer) {
        CustomizedJob job = CUSTOMIZED_JENKINS_JOBS.get(id);
        job.setJenkinsServer(jenkinsServer);
        job.setJenkinsJobStatus(JobStatus.JENKINS_RUNNING);
    }

    @Override
    public void setDockerClientInfo(Long id, DockerClient dockerClient, String containerId) {
        CustomizedJob job = CUSTOMIZED_JENKINS_JOBS.get(id);
        job.setDockerClient(dockerClient);
        job.setContainerId(containerId);
        job.setJenkinsJobStatus(JobStatus.DOCKER_RUNNING);
    }

    @Override
    public void jenkinsBuildComplete(Long id) {
        CustomizedJob job = CUSTOMIZED_JENKINS_JOBS.get(id);
        job.setJenkinsJobStatus(JobStatus.JENKINS_COMPLETE);
    }

    @Override
    public void dockerContainerStartComplete(Long id) {
        CustomizedJob job = CUSTOMIZED_JENKINS_JOBS.get(id);
        job.setJenkinsJobStatus(JobStatus.ALL_COMPLETE);
    }

    @Override
    public List<CustomizedJobVO> listJobStatus() {
        return CUSTOMIZED_JENKINS_JOBS.values().stream().map(e -> {
            CustomizedJobVO vo = new CustomizedJobVO();
            vo.setContainerId(e.getContainerId());
            vo.setProjectId(e.getProjectSourceDO().getId());
            vo.setStatus(e.getJenkinsJobStatus());
            return vo;
        }).collect(Collectors.toList());
    }
}
