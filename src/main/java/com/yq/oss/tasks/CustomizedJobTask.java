package com.yq.oss.tasks;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.yq.oss.entity.domain.CustomizedJob;
import com.yq.oss.entity.dto.JenkinsSource;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.enums.JenkinsJobStatus;
import com.yq.oss.service.ProjectRunningService;
import com.yq.oss.service.impl.ProjectRunningServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomizedJobTask {
    @Autowired
    private ProjectRunningService projectRunningService;

    @Scheduled(fixedRate = 5000L)
    public void executeCustomizedJobTask() {
        Collection<CustomizedJob> jobs = ProjectRunningServiceImpl.CUSTOMIZED_JENKINS_JOBS.values();
        jobs.forEach(this::executeCustomizedJob);
    }

    private void executeCustomizedJob(CustomizedJob job) {
        log.info("executeCustomizedJob");
        try {
            if (job.getJenkinsJobStatus() == JenkinsJobStatus.NOT_RUNNING) {
                //未启动,则创建jenkins任务
                runJenkinsBuild(job);
            } else if (job.getJenkinsJobStatus() == JenkinsJobStatus.JENKINS_RUNNING) {
                //jenkins任务正在运行,检查运行状态
                checkJenkinsBuild(job);
            } else if (job.getJenkinsJobStatus() == JenkinsJobStatus.JENKINS_COMPLETE) {
                log.info("JENKINS_COMPLETE");
            }
        } catch (Exception e) {
            log.error("executeCustomizedJob error", e);
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
