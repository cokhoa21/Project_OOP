package com.example.model;

public class BinanceItem {
	private String name;
	private String acronym;
	private String price;
	private String volume;
	
	public BinanceItem(String name, String acronym, String price, String volume) {
		super();
		this.name = name;
		this.acronym = acronym;
		this.price = price;
		this.volume = volume;
	}
	
	public BinanceItem() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	
}
