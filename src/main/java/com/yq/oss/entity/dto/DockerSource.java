package com.yq.oss.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("docker_source")
public class DockerSource {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String host;

    private String name;
}
