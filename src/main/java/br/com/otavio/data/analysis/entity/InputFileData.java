package br.com.otavio.data.analysis.entity;

import java.util.List;

public class InputFileData {

	private final List<Customer> customers;
	private final List<Sale> sales;
	private final List<Salesman> salesmen;

	public InputFileData(final List<Customer> customers, final List<Sale> sales, final List<Salesman> salesmen) {
		this.customers = customers;
		this.sales = sales;
		this.salesmen = salesmen;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public List<Salesman> getSalesmen() {
		return salesmen;
	}
}