package org.enthusa.askdata.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.enthusa.avatar.mybatis.helper.MapperHelper;
import org.enthusa.avatar.mybatis.helper.PageInterceptor;
import org.enthusa.avatar.mybatis.mapper.SqlMapper;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author henry
 * @date 2023/6/24
 */
@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MapperConfiguration {
    @Resource
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void addMapperInterceptor() {
        MapperHelper mapperHelper = new MapperHelper();
        PageInterceptor interceptor = new PageInterceptor();
        applicationContext.getBeansOfType(SqlMapper.class);
        mapperHelper.registerMapper(SqlMapper.class);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
            mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());
        }
    }
}
