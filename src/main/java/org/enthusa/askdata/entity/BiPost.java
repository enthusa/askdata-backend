package org.enthusa.askdata.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BiPost {
    /**
     * id
     */
    private Integer id;

    /**
     * title
     */
    private String title;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }
}
