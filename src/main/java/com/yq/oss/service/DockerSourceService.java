package com.yq.oss.service;

import com.yq.oss.entity.dto.DockerSource;

import java.util.List;

public interface DockerSourceService {
    List<DockerSource> list();

    void add(DockerSource dockerSource);

    void edit(DockerSource dockerSource);

    void addOrEdit(DockerSource dockerSource);

    void delete(Long id);

    DockerSource findById(Long id);
}
