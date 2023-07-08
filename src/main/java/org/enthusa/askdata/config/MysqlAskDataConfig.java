package org.enthusa.askdata.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.enthusa.askdata.common.Consts;
import org.enthusa.avatar.mybatis.utils.DbUtil;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

/**
 * @author henry
 * @date 2023/2/18
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "org.enthusa.askdata.mapper", sqlSessionFactoryRef = "askDataSqlSessionFactory")
public class MysqlAskDataConfig {
    private static final String MAPPER_LOCATION = "classpath*:org/enthusa/askdata/mapper/*Mapper.xml";

    @Autowired
    private GlobalSetting globalSetting;

    @Bean
    @Primary
    public DataSource askDataDataSource() {
        String dbHost = StringUtils.defaultIfBlank(System.getenv("AD_DB_HOST"), globalSetting.getDbHost());
        String dbPort = StringUtils.defaultIfBlank(System.getenv("AD_DB_PORT"), globalSetting.getDbPort());
        String dbName = StringUtils.defaultIfBlank(System.getenv("AD_DB_NAME"), globalSetting.getDbName());
        String username = StringUtils.defaultIfBlank(System.getenv("AD_DB_USER"), globalSetting.getDbUsername());
        String password = StringUtils.defaultIfBlank(System.getenv("AD_DB_PASS"), globalSetting.getDbPassword());

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?%s", dbHost, dbPort, dbName, Consts.MYSQL_JDBC_URL_DECORATOR);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    @Primary
    public SqlSessionFactory askDataSqlSessionFactory(@Qualifier("askDataDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setConfiguration(DbUtil.getMyBatisConfig());
        sessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resourceResolver.getResources(MAPPER_LOCATION);
        } catch (FileNotFoundException e) {
            log.info("no mapper file found in mapperLocation:{}", MAPPER_LOCATION);
        }
        if (resources != null) {
            sessionFactoryBean.setMapperLocations(resources);
        }
        return sessionFactoryBean.getObject();
    }
}
