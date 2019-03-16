package com.itheima.domain;



public class CartItem {
	private Product product;
	private int byNum;
	private double subtotal;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getByNum() {
		return byNum;
	}
	public void setByNum(int byNum) {
		this.byNum = byNum;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
}
