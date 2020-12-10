package br.com.otavio.data.analysis.entity;

import java.math.BigDecimal;

public class SaleItem {

	private final String id;
	private final int quantity;
	private final BigDecimal price;

	public SaleItem(final String id, final int quantity, final BigDecimal price) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}
}