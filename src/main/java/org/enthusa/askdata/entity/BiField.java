package org.enthusa.askdata.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BiField {
    /**
     * id
     */
    private Integer id;

    /**
     * 数据表 id
     */
    private Integer tableId;

    /**
     * 列顺序, 序号
     */
    private Integer columnSeq;

    /**
     * 主键顺序: 0, 表示非主键
     */
    private Integer primaryKeySeq;

    /**
     * 外键, 参考字段
     */
    private Integer referId;

    /**
     * 字段名, 英文名
     */
    private String name;

    /**
     * 字段简介, 中文名
     */
    private String brief;

    /**
     * 字段注释, 详解
     */
    private String remarks;

    /**
     * 字段类型
     */
    private String typeName;

    /**
     * 字段长度
     */
    private Integer columnSize;

    /**
     * 是否自增
     */
    private Boolean isAutoIncrement;

    /**
     * 是否允许为空
     */
    private Boolean isNullable;

    /**
     * 字段默认值
     */
    private String defaultValue;

    /**
     * 是否可枚举
     */
    private Boolean isEnumerable;

    /**
     * 枚举值列表, 格式: value->label
     */
    private String options;

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

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue == null ? null : defaultValue.trim();
    }

    public void setOptions(String options) {
        this.options = options == null ? null : options.trim();
    }
}
