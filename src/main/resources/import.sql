DROP TABLE IF EXISTS `bi_post`;
CREATE TABLE `bi_post` (
  `id`          INT      NOT NULL AUTO_INCREMENT,
  `title`       VARCHAR(255)      DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
INSERT INTO bi_post (title) VALUE ('测试帖子');

DROP TABLE IF EXISTS `bi_data_source`;
CREATE TABLE `bi_data_source` (
  `id`          INT(11)     NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(64) NOT NULL COMMENT '数据源名称',
  `details`     TEXT COMMENT '数据源连接信息',
  `catalogs`    VARCHAR(255)         DEFAULT NULL COMMENT '该数据源下, 可用的数据库, 多个逗号连接',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_update_time` (`update_time`)
);

DROP TABLE IF EXISTS `bi_table`;
CREATE TABLE `bi_table` (
  `id`              INT(11)      NOT NULL AUTO_INCREMENT,
  `ds_id`           INT(11)      NOT NULL COMMENT '数据源 id',
  `catalog`         VARCHAR(32)  NOT NULL COMMENT '目录, 相当于数据库',
  `name`            VARCHAR(128) NOT NULL COMMENT '表名, 英文名',
  `brief`           VARCHAR(16)           DEFAULT NULL COMMENT '表简介, 中文名',
  `remarks`         TEXT COMMENT '表注释, 详解',
  `owner_id`        INT(11)               DEFAULT NULL COMMENT '责任人',
  `update_user_id`  INT(11)               DEFAULT NULL COMMENT '最近修改人',
  `create_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status`          INT(11)               DEFAULT '0' COMMENT '是否生效 0生效 1失效',
  `query_cnt`       INT(11)      NOT NULL DEFAULT '0' COMMENT '近三个月查询次数',
  `query_user_cnt`  INT(11)      NOT NULL DEFAULT '0' COMMENT '近三个月查询人数',
  `last_query_time` DATETIME              DEFAULT NULL COMMENT '最后一次查询时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_on_ds_catalog_name` (`ds_id`, `catalog`, `name`),
  KEY `idx_update_time` (`update_time`)
);

DROP TABLE IF EXISTS `bi_field`;
CREATE TABLE `bi_field` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT,
  `table_id`          INT(11)     NOT NULL COMMENT '数据表 id',
  `column_seq`        INT(11)     NOT NULL COMMENT '列顺序, 序号',
  `primary_key_seq`   INT(11)     NOT NULL DEFAULT '0' COMMENT '主键顺序: 0, 表示非主键',
  `refer_id`          INT(11)              DEFAULT NULL COMMENT '外键, 参考字段',
  `name`              VARCHAR(64) NOT NULL COMMENT '字段名, 英文名',
  `brief`             VARCHAR(16)          DEFAULT NULL COMMENT '字段简介, 中文名',
  `remarks`           TEXT COMMENT '字段注释, 详解',
  `type_name`         VARCHAR(32)          DEFAULT NULL COMMENT '字段类型',
  `column_size`       INT(11)              DEFAULT NULL COMMENT '字段长度',
  `is_auto_increment` TINYINT(1)           DEFAULT NULL COMMENT '是否自增',
  `is_nullable`       TINYINT(1)           DEFAULT NULL COMMENT '是否允许为空',
  `default_value`     VARCHAR(64)          DEFAULT NULL COMMENT '字段默认值',
  `is_enumerable`     TINYINT(1)           DEFAULT NULL COMMENT '是否可枚举',
  `options`           TEXT COMMENT '枚举值列表, 格式: value->label',
  `update_user_id`    INT(11)              DEFAULT NULL COMMENT '最近修改人',
  `create_time`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status`            INT(11)              DEFAULT '0' COMMENT '是否生效 0生效 1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_on_table_name` (`table_id`, `name`),
  KEY `idx_update_time` (`update_time`),
  KEY `idx_on_name` (`name`)
);