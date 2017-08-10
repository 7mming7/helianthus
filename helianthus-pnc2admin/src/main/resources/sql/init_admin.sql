CREATE DATABASE helianthus;

CREATE TABLE t_Parameter (
  id int(11) NOT NULL AUTO_INCREMENT,
  attribute varchar(100) NOT NULL,
  description varchar(100) NOT NULL,
  ptype varchar(100),
  value varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE t_Dictionary (
  id int(11) NOT NULL AUTO_INCREMENT,
  attribute varchar(100) NOT NULL,
  description varchar(100) NOT NULL,
  tableName varchar(100),
  value INTEGER NOT NULL,
  serialNo INTEGER NOT NULL,
  status INTEGER NOT null,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
