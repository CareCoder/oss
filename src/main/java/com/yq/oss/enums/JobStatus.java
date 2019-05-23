package com.yq.oss.enums;

public enum JobStatus {
    READY_RUNNING, //还未开始启动
    JENKINS_RUNNING,// jenkins正在制作镜像
    JENKINS_COMPLETE,// jenkins制作镜像完成
    DOCKER_RUNNING,//docker正在启动
    DOCKER_COMPLETE,//docker启动完成
    JENKINS_FAIL,//jenkins正在制作镜像的时候失败了
    DOCKER_FAIL,//docker启动的时候失败了
    ALL_COMPLETE//启动成功
}
