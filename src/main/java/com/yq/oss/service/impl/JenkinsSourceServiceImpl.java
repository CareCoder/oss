package com.yq.oss.service.impl;

import com.yq.oss.entity.dto.JenkinsSource;
import com.yq.oss.mapper.JenkinsSourceMapper;
import com.yq.oss.service.JenkinsSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JenkinsSourceServiceImpl implements JenkinsSourceService {
    @Autowired
    private JenkinsSourceMapper jenkinsSourceMapper;

    @Override
    public List<JenkinsSource> list() {
        return jenkinsSourceMapper.selectList(null);
    }

    @Override
    public void add(JenkinsSource jenkinsSource) {
        jenkinsSourceMapper.insert(jenkinsSource);
    }

    @Override
    public void edit(JenkinsSource jenkinsSource) {
        jenkinsSourceMapper.updateById(jenkinsSource);
    }

    @Override
    public void addOrEdit(JenkinsSource jenkinsSource) {
        if (jenkinsSource.getId() == null) {
            add(jenkinsSource);
        }else{
            edit(jenkinsSource);
        }
    }

    @Override
    public void delete(Long id) {
        jenkinsSourceMapper.deleteById(id);
    }

    @Override
    public JenkinsSource findById(Long id) {
        return jenkinsSourceMapper.selectById(id);
    }
}
