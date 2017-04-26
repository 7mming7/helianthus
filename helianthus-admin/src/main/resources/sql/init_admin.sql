CREATE DATABASE helianthus;

CREATE TABLE t_siteAccount (
  id int(11) NOT NULL AUTO_INCREMENT,
  userName varchar(100) NOT NULL,
  passWord varchar(100) NOT NULL,
  domain varchar(100) NOT NULL,
  available varchar(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE t_parameter (
  id int(11) NOT NULL AUTO_INCREMENT,
  attribute varchar(100) NOT NULL,
  description varchar(100) NOT NULL,
  ptype varchar(100),
  value varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE t_YearBillMailRecord (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  content varchar(255) DEFAULT NULL,
  contentType int(11) DEFAULT NULL,
  execDate datetime DEFAULT NULL,
  msg varchar(255) DEFAULT NULL,
  reciver varchar(255) DEFAULT NULL,
  subject varchar(255) DEFAULT NULL,
  success bit(1) NOT NULL,
  billDate varchar(255) DEFAULT NULL,
  billType int(11) DEFAULT NULL,
  year varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE t_fileStore (
  id int(11) NOT NULL AUTO_INCREMENT,
  cid VARCHAR(255) NOT NULL,
  fileType int(1) NOT NULL,
  content mediumblob  NOT NULL,
  uploadTime datetime NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
