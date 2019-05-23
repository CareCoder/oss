package com.yq.oss.entity.vo;

import com.yq.oss.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedJobVO {
    private Long projectId;
    private JobStatus status;
    private String containerId;
}
