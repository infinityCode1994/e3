package com.itheima.domain;

public class Category {
/*	-- Table structure for table `category`
	--

	DROP TABLE IF EXISTS `category`;
	!40101 SET @saved_cs_client     = @@character_set_client ;
	!40101 SET character_set_client = utf8 ;
	CREATE TABLE `category` (
	  `cid` varchar(32) NOT NULL,
	  `cname` varchar(20) DEFAULT NULL,
	  PRIMARY KEY (`cid`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	!40101 SET character_set_client = @saved_cs_client ;

	--
	-- Dumping data for table `category`*/
	private String cid;
	private String cname;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}

}
