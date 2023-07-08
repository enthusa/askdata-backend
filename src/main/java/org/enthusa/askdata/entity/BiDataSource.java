package org.enthusa.askdata.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.enthusa.askdata.common.Consts;
import org.enthusa.avatar.core.consts.TextConstant;
import org.enthusa.avatar.mybatis.annotation.Transient;

import java.util.*;

@Data
public class BiDataSource {
    /**
     * id
     */
    private Integer id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源连接信息
     */
    private String details;

    @Transient
    private String dbHost;
    @Transient
    private String dbPort;
    @Transient
    private String dbName;
    @Transient
    private String jdbcUrl;
    @Transient
    private String user;
    @Transient
    private String password;

    /**
     * 该数据源下, 可用的数据库, 多个逗号连接
     */
    private String catalogs;

    @Transient
    private List<String> catalogList;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }

    public void setCatalogs(String catalogs) {
        this.catalogs = catalogs == null ? null : catalogs.trim();
    }

    public void fillDerivedFieldsFromDatabase() {
        catalogList = TextConstant.COMMA_SPLITTER.splitToList(StringUtils.defaultString(catalogs));

        byte[] bytes = Base64.getDecoder().decode(details);
        Properties info = JSON.parseObject(new String(bytes), Properties.class);
        dbHost = info.getProperty("dbHost");
        dbPort = info.getProperty("dbPort");
        dbName = info.getProperty("dbName");
        jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?%s", dbHost, dbPort, dbName, Consts.MYSQL_JDBC_URL_DECORATOR);
        user = info.getProperty("user");
        password = info.getProperty("password");
    }

    public void convertToDatabaseValue() {
        catalogs = TextConstant.COMMA_JOINER.join(Optional.ofNullable(catalogList).orElse(Collections.emptyList()));

        Properties config = new Properties();
        config.setProperty("dbHost", dbHost.trim());
        config.setProperty("dbPort", dbPort.trim());
        config.setProperty("dbName", dbName.trim());
        config.setProperty("user", user.trim());
        config.setProperty("password", password.trim());
        String text = JSON.toJSONString(config);
        details = Base64.getEncoder().encodeToString(text.getBytes());
    }
}
