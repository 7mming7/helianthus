CREATE TABLE t_Node (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  description varchar(1000) NOT NULL,
  active tinyint NOT NULL DEFAULT 1,
  projectId int(11) NOT NULL,
  flowId int(11) NOT NULL,
  jobType int(2) NOT NULL,
  executeStatus  int(2),
  level  int(10),
  depends varchar(1000),
  params varchar(1000),
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE t_Project (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  description varchar(1000) NOT NULL,
  active tinyint NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE t_Flow (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  description varchar(1000) NOT NULL,
  active tinyint NOT NULL DEFAULT 1,
  projectId int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
