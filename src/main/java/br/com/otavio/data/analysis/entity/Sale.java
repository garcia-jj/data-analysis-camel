package br.com.otavio.data.analysis.entity;

import java.util.List;

public class Sale {

	private final String id;
	private final List<SaleItem> items;
	private final String salesmanName;

	public Sale(final String id, final List<SaleItem> items, final String salesmanName) {
		this.id = id;
		this.items = items;
		this.salesmanName = salesmanName;
	}

	public String getId() {
		return id;
	}

	public List<SaleItem> getItems() {
		return items;
	}

	public String getSalesmanName() {
		return salesmanName;
	}
}