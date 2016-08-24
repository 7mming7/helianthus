CREATE TABLE t_site_account (
  id int(11) NOT NULL AUTO_INCREMENT,
  userName varchar(100) NOT NULL,
  passWord varchar(100) NOT NULL,
  domain varchar(100) NOT NULL,
  available varchar(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
