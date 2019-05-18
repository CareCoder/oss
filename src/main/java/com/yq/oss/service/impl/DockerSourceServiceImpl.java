package com.yq.oss.service.impl;

import com.yq.oss.entity.dto.DockerSource;
import com.yq.oss.mapper.DockerSourceMapper;
import com.yq.oss.service.DockerSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerSourceServiceImpl implements DockerSourceService {
    @Autowired
    private DockerSourceMapper dockerSourceMapper;

    @Override
    public List<DockerSource> list() {
        return dockerSourceMapper.selectList(null);
    }

    @Override
    public void add(DockerSource dockerSource) {
        dockerSourceMapper.insert(dockerSource);
    }

    @Override
    public void edit(DockerSource dockerSource) {
        dockerSourceMapper.updateById(dockerSource);
    }

    @Override
    public void addOrEdit(DockerSource dockerSource) {
        if (dockerSource.getId() == null) {
            add(dockerSource);
        }else{
            edit(dockerSource);
        }
    }

    @Override
    public void delete(Long id) {
        dockerSourceMapper.deleteById(id);
    }

    @Override
    public DockerSource findById(Long id) {
        return dockerSourceMapper.selectById(id);
    }
}
