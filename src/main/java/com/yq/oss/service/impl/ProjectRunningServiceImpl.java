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

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectRunningServiceImpl implements ProjectRunningService {
    private static Map<Long, CustomizedJob> CUSTOMIZED_JOBS = new ConcurrentHashMap<>();
    @Override
    public void run(ProjectSourceDO projectSourceDO) {
        CUSTOMIZED_JOBS.putIfAbsent(projectSourceDO.getId(), CustomizedJob.buildDefault(projectSourceDO, JobStatus.READY_RUNNING));
    }

    @Override
    public void restart(Long projectId) {
        CustomizedJob job = CUSTOMIZED_JOBS.get(projectId);
        DockerClient dockerClient = job.getDockerClient();
        dockerClient.restartContainerCmd(job.getContainerId()).exec();
        job.setJenkinsJobStatus(JobStatus.DOCKER_RUNNING);
    }

    @Override
    public void stop(Long projectId) {
        CUSTOMIZED_JOBS.remove(projectId);
        CustomizedJob job = CUSTOMIZED_JOBS.get(projectId);
        DockerClient dockerClient = job.getDockerClient();
        dockerClient.stopContainerCmd(job.getContainerId()).exec();
    }

    @Override
    public Collection<CustomizedJob> fetchCustomizedJob() {
        return CUSTOMIZED_JOBS.values();
    }

    @Override
    public void setJobJenkinsServer(Long id, JenkinsServer jenkinsServer) {
        CustomizedJob job = CUSTOMIZED_JOBS.get(id);
        job.setJenkinsServer(jenkinsServer);
        job.setJenkinsJobStatus(JobStatus.JENKINS_RUNNING);
    }

    @Override
    public void setDockerClientInfo(Long id, DockerClient dockerClient, String containerId) {
        CustomizedJob job = CUSTOMIZED_JOBS.get(id);
        job.setDockerClient(dockerClient);
        job.setContainerId(containerId);
        job.setJenkinsJobStatus(JobStatus.DOCKER_RUNNING);
    }

    @Override
    public void jenkinsBuildComplete(Long id) {
        CustomizedJob job = CUSTOMIZED_JOBS.get(id);
        job.setJenkinsJobStatus(JobStatus.JENKINS_COMPLETE);
    }

    @Override
    public void dockerContainerStartComplete(Long id) {
        CustomizedJob job = CUSTOMIZED_JOBS.get(id);
        job.setJenkinsJobStatus(JobStatus.ALL_COMPLETE);
    }

    @Override
    public Map<Long, CustomizedJobVO> fetchJobStatusMap() {
        return CUSTOMIZED_JOBS.values().stream().map(e -> {
            CustomizedJobVO vo = new CustomizedJobVO();
            vo.setContainerId(e.getContainerId());
            vo.setProjectId(e.getProjectSourceDO().getId());
            vo.setStatus(e.getJenkinsJobStatus());
            return vo;
        }).collect(Collectors.toMap(CustomizedJobVO::getProjectId, e -> e));
    }
}
