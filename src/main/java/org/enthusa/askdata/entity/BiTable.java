package org.enthusa.askdata.entity;

import lombok.Data;
import org.enthusa.avatar.mybatis.annotation.Transient;

import java.util.Date;

@Data
public class BiTable {
    /**
     * id
     */
    private Integer id;

    /**
     * 数据源 id
     */
    private Integer dsId;

    /**
     * 目录, 相当于数据库
     */
    private String catalog;

    /**
     * 表名, 英文名
     */
    private String name;

    /**
     * 表简介, 中文名
     */
    private String brief;

    /**
     * 表注释, 详解
     */
    private String remarks;

    /**
     * 责任人
     */
    private Integer ownerId;

    /**
     * 最近修改人
     */
    private Integer updateUserId;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    /**
     * 是否生效： 0:生效； 1:失效
     */
    private Integer status;


    /**
     * query_cnt 近三个月查询次数
     */
    private Integer queryCnt;

    /**
     * query_user_cnt 近三个月查询人数
     */
    private Integer queryUserCnt;

    /**
     * last_query_time
     */
    private Date lastQueryTime;

    @Transient
    private String columnNames = "";
    @Transient
    private String columnBriefs = "";
    @Transient
    private String columnRemarks = "";

    public void setCatalog(String catalog) {
        this.catalog = catalog == null ? null : catalog.trim();
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}
