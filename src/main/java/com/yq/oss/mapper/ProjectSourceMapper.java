package com.yq.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yq.oss.entity.dto.ProjectSource;
import com.yq.oss.entity.vo.ProjectSourceDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ProjectSourceMapper extends BaseMapper<ProjectSource> {

    @Results(id="ProjectSourceDO", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "gitAddress", column = "git_address"),
            @Result(property = "version", column = "version"),
            @Result(property = "imageName", column = "image_name"),
            @Result(property = "env", column = "env"),
            @Result(property = "port", column = "port"),
            @Result(property = "dockerSource", column = "docker_source_id", one = @One(select = "com.yq.oss.mapper.DockerSourceMapper.selectById")),
            @Result(property = "jenkinsSource", column = "jenkins_source_id", one = @One(select = "com.yq.oss.mapper.JenkinsSourceMapper.selectById"))
    })
    @Select("SELECT * from project_source")
    List<ProjectSourceDO> list();

    @ResultMap("ProjectSourceDO")
    @Select("SELECT * from project_source where id = #{id}")
    ProjectSourceDO findDOById(Long id);
}
