package com.yq.oss.service.impl;

import com.yq.oss.entity.dto.ProjectSource;
import com.yq.oss.entity.vo.CustomizedJobVO;
import com.yq.oss.entity.vo.ProjectSourceDO;
import com.yq.oss.mapper.ProjectSourceMapper;
import com.yq.oss.service.ProjectRunningService;
import com.yq.oss.service.ProjectSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectSourceServiceImpl implements ProjectSourceService {
    @Autowired
    private ProjectSourceMapper projectSourceMapper;
    @Autowired
    private ProjectRunningService projectRunningService;

    @Override
    public List<ProjectSourceDO> list() {
        return projectSourceMapper.list();
    }

    @Override
    public void add(ProjectSource projectSource) {
        projectSourceMapper.insert(projectSource);
    }

    @Override
    public void edit(ProjectSource projectSource) {
        projectSourceMapper.updateById(projectSource);
    }

    @Override
    public void addOrEdit(ProjectSource projectSource) {
        if (projectSource.getId() == null) {
            add(projectSource);
        }else{
            edit(projectSource);
        }
    }

    @Override
    public void delete(Long id) {
        projectSourceMapper.deleteById(id);
    }

    @Override
    public ProjectSource findById(Long id) {
        return projectSourceMapper.selectById(id);
    }

    @Override
    public ProjectSourceDO findDOById(Long id) {
        return projectSourceMapper.findDOById(id);
    }

    @Override
    public List<ProjectSourceDO> listDetail() {
        List<ProjectSourceDO> projectSourceDOList = list();
        Map<Long, CustomizedJobVO> jobVOMap = projectRunningService.fetchJobStatusMap();
        projectSourceDOList.forEach(e -> {
            CustomizedJobVO jobVO = jobVOMap.get(e.getId());
            if (jobVO != null) {
                e.setJobStatus(jobVO.getStatus());
            }
        });
        return projectSourceDOList;
    }
}
