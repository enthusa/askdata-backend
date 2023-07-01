package org.enthusa.askdata.entity;

import lombok.Data;
import org.enthusa.avatar.mybatis.annotation.Transient;

import java.util.Date;
import java.util.List;

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
    private String url;
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
}
