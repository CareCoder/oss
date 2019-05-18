package com.yq.oss.configurer;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MybatisConfig
 * @Description
 * @Author LiuYin
 * @Date 2019/1/29 14:25
 */
@Configuration
@MapperScan({"com.yq.oss.mapper"})
public class MybatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

//    @Autowired
//    private SqlSessionFactory sqlSessionFactory;
//
//    @Bean
//    public MybatisSqlBuilder mybatisSqlBuilder(){
//        final org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
//        System.out.println(configuration);
//        final MapperAndDomain mapperAndDomain = new MapperAndDomain();
//        mapperAndDomain.setMapperType(LaneMapper.class);
//        mapperAndDomain.setDomainType(LaneDO.class);
//
//        final MapperAndDomain mapperAndDomain2 = new MapperAndDomain();
//        mapperAndDomain2.setMapperType(TunnelMapper.class);
//        mapperAndDomain2.setDomainType(PointDO.class);
//
//        MybatisSqlBuilder.from(sqlSessionFactory.getConfiguration(),mapperAndDomain);
//        System.out.println(configuration.isMapUnderscoreToCamelCase());
//        return new MybatisSqlBuilder();
//    }
}
