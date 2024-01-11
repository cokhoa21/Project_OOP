package com.example.model;

public class TrendingNFT {
	private String title;
	private String price;
	private String creator;
	private String editions;
	
	public TrendingNFT(String title, String price, String creator, String editions) {
		super();
		this.title = title;
		this.price = price;
		this.creator = creator;
		this.editions = editions;
	}
	
	public TrendingNFT() {
		super();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getEditions() {
		return editions;
	}
	public void setEditions(String editions) {
		this.editions = editions;
	}
	
	
	
}
