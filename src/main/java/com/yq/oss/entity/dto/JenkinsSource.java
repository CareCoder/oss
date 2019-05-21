package com.yq.oss.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("jenkins_source")
public class JenkinsSource {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String host;

    private String name;

    private String projectName;

    private String token;
}
