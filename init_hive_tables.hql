-- 创建ha数据库
create database ha;

-- 使用ha数据库
use ha;

-- 建表语句
create external table ORIGNAL_DATA(
	ID string COMMENT '唯一标示'
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\001'
STORED AS TEXTFILE
location '/user/hive/warehouse/ha.db/ORIGNAL_DATA';