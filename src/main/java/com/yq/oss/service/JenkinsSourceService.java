package com.yq.oss.service;

import com.yq.oss.entity.dto.JenkinsSource;

import java.util.List;

public interface JenkinsSourceService {
    List<JenkinsSource> list();

    void add(JenkinsSource jenkinsSource);

    void edit(JenkinsSource jenkinsSource);

    void addOrEdit(JenkinsSource jenkinsSource);

    void delete(Long id);

    JenkinsSource findById(Long id);
}
