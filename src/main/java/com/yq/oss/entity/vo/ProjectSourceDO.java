package com.yq.oss.entity.vo;

import com.yq.oss.entity.dto.DockerSource;
import com.yq.oss.entity.dto.JenkinsSource;
import com.yq.oss.enums.JobStatus;
import lombok.Data;

@Data
public class ProjectSourceDO {
    private Long id;

    private String name;

    private String gitAddress;

    private String version;

    private String imageName;

    private String imageTag;

    private String env;

    private Integer port;

    private DockerSource dockerSource;

    private JenkinsSource jenkinsSource;

    private JobStatus jobStatus;

    public String fetchRepoTags() {
        return imageName + ":" + imageTag;
    }
}
