package com.itheima.domain;

public class OrderItem {
//	`itemid` varchar(32) NOT NULL,
//	  `count` int(11) DEFAULT NULL,
//	  `subtotal` double DEFAULT NULL,
//	  `pid` varchar(32) DEFAULT NULL,
//	  `oid` varchar(32) DEFAULT NULL,
//	  PRIMARY KEY (`itemid`),
//	  KEY `fk_0001` (`pid`),
//	  KEY `fk_0002` (`oid`),
//	  CONSTRAINT `fk_0001` FOREIGN KEY (`pid`) REFERENCES `product` (`pid`),
//	  CONSTRAINT `fk_0002` FOREIGN KEY (`oid`) REFERENCES `orders` (`oid`)
	private String itemid;
	private int count;
	private double subtotal;
//	private String pid;
//	private String oid;
	private Product product;
	private Order order;
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
