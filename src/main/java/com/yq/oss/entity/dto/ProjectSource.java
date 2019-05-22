package com.yq.oss.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("project_source")
public class ProjectSource {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String gitAddress;

    private String version;

    private String imageName;

    private String imageTag;

    private String env;

    private Integer port;

    private Long dockerSourceId;

    private Long jenkinsSourceId;
}
