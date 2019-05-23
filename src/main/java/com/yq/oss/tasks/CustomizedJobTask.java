package com.yq.oss.tasks;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Ports;
import com.google.common.base.Strings;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.yq.oss.entity.domain.CustomizedJob;
import com.yq.oss.entity.dto.JenkinsSource;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.enums.JobStatus;
import com.yq.oss.service.ProjectRunningService;
import com.yq.oss.utils.DockerClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomizedJobTask {
    @Autowired
    private ProjectRunningService projectRunningService;

    @Scheduled(fixedRate = 5000L)
    public void executeCustomizedJobTask() {
        Collection<CustomizedJob> jobs = projectRunningService.fetchCustomizedJob();
        jobs.forEach(this::executeCustomizedJob);
    }

    private void executeCustomizedJob(CustomizedJob job) {
        ProjectSourceDO projectSourceDO = job.getProjectSourceDO();
        log.info("executeCustomizedJob id = {}, status = {}", projectSourceDO.getId(), job.getJenkinsJobStatus());
        try {
            if (job.getJenkinsJobStatus() == JobStatus.READY_RUNNING) {
                //未启动,则创建jenkins任务
                runJenkinsBuild(job);
            } else if (job.getJenkinsJobStatus() == JobStatus.JENKINS_RUNNING) {
                //jenkins任务正在运行,检查运行状态
                checkJenkinsBuild(job);
            } else if (job.getJenkinsJobStatus() == JobStatus.JENKINS_COMPLETE) {
                //运行docker启动容器
                runDockerStartContainer(job);
            } else if (job.getJenkinsJobStatus() == JobStatus.DOCKER_RUNNING) {
                //尝试检测docker container是否启动成功
                checkDockerContainer(job);
            }
        } catch (Exception e) {
            log.error("executeCustomizedJob error", e);
        }
//        projectRunningService.dockerContainerStartComplete(job.getProjectSourceDO().getId());
    }

    private void checkDockerContainer(CustomizedJob job) {
        DockerClient dockerClient = job.getDockerClient();
        List<Container> containers = dockerClient.listContainersCmd().exec();
        Optional<Container> anyContainer = containers.stream().filter(e -> e.getId().equals(job.getContainerId())).findAny();
        anyContainer.ifPresent(e -> projectRunningService.dockerContainerStartComplete(job.getProjectSourceDO().getId()));
    }

    private void runDockerStartContainer(CustomizedJob job) {
        ProjectSourceDO projectSourceDO = job.getProjectSourceDO();
        DockerClient dockerClient = DockerClientUtils.buildDefault(projectSourceDO.getDockerSource().getHost());
        //1.先读取快照,看看image是否创建,正常流程应该是创建好了的,如果没创建好,则应该是流程出问题了
        List<Image> images = dockerClient.listImagesCmd().exec();
        Optional<Image> anyImage = images.stream().filter(e -> {
            String[] repoTags = e.getRepoTags();
            boolean flag = false;
            for (String repoTag : repoTags) {
                if (repoTag.equals(projectSourceDO.fetchRepoTags())) {
                    flag = true;
                }
            }
            return flag;
        }).findAny();
        String imageId = null;
        if (anyImage.isPresent()) {
            //2.根据这个image的id 查看是否已经有启动好了的container,如果有则删除container
            imageId = anyImage.get().getId();
            List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
            String finalImageId = imageId;
            List<Container> toDeleteContains = containers.stream().filter(e -> Objects.equals(e.getImageId(), finalImageId)).collect(Collectors.toList());
            toDeleteContains.forEach(e -> dockerClient.removeContainerCmd(e.getId()).withForce(true).exec());
        }
        //3.创建启动新的container
        if (!Strings.isNullOrEmpty(imageId)) {
            ExposedPort exposedPort = ExposedPort.tcp(projectSourceDO.getPort());
            Ports portBindings = new Ports();
            portBindings.bind(ExposedPort.tcp(projectSourceDO.getPort()), Ports.Binding.bindPort(projectSourceDO.getPort()));
            String[] envs = projectSourceDO.getEnv().split(" ");
            String containerName = projectSourceDO.getName() + "-" + projectSourceDO.getImageTag();
            CreateContainerResponse createContainer = dockerClient.createContainerCmd(imageId)
                    .withName(containerName)
                    .withEnv(envs)
                    .withPortBindings(portBindings)
                    .withExposedPorts(exposedPort)
                    .exec();
            dockerClient.startContainerCmd(createContainer.getId()).exec();
            projectRunningService.setDockerClientInfo(projectSourceDO.getId(), dockerClient, createContainer.getId());
        }
    }

    private void checkJenkinsBuild(CustomizedJob job) throws IOException {
        ProjectSourceDO projectSourceDO = job.getProjectSourceDO();
        JenkinsSource jenkinsSource = projectSourceDO.getJenkinsSource();
        JobWithDetails jobWithDetails = job.getJenkinsServer().getJob(jenkinsSource.getProjectName());
        BuildWithDetails lastBuildWithDetails = jobWithDetails.getLastBuild().details();
        if (! lastBuildWithDetails.isBuilding()) {
            //完成jenkins building
            projectRunningService.jenkinsBuildComplete(job.getProjectSourceDO().getId());
        }
    }

    private void runJenkinsBuild(CustomizedJob job) throws Exception {
        ProjectSourceDO projectSourceDO = job.getProjectSourceDO();
        JenkinsSource jenkinsSource = projectSourceDO.getJenkinsSource();
        String jenkinsHost = jenkinsSource.getHost();
        if (!jenkinsHost.startsWith("http")) {
            jenkinsHost = "http://" + jenkinsHost;
        }
        JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsHost), jenkinsSource.getUsername(), jenkinsSource.getPassword());
        JobWithDetails jobWithDetails = jenkinsServer.getJob(jenkinsSource.getProjectName());
        jobWithDetails.build(buildJenkinsParams(projectSourceDO));
        projectRunningService.setJobJenkinsServer(job.getProjectSourceDO().getId(), jenkinsServer);
    }

    private Map<String,String> buildJenkinsParams(ProjectSourceDO projectSourceDO) {
        Map<String, String> params = new HashMap<>();
        params.put("token", projectSourceDO.getJenkinsSource().getToken());
        params.put("version", projectSourceDO.getVersion());
        params.put("address", projectSourceDO.getGitAddress());
        params.put("image", projectSourceDO.getImageName() + ":" + projectSourceDO.getImageTag());
        return params;
    }
}
