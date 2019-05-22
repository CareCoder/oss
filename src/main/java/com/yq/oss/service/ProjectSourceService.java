package com.yq.oss.service;

import com.yq.oss.entity.dto.ProjectSource;
import com.yq.oss.entity.vo.ProjectSourceDO;

import java.util.List;

public interface ProjectSourceService {
    List<ProjectSourceDO> list();

    void add(ProjectSource projectSource);

    void edit(ProjectSource projectSource);

    void addOrEdit(ProjectSource projectSource);

    void delete(Long id);

    ProjectSource findById(Long id);

    ProjectSourceDO findDOById(Long id);
}
