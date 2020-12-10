package br.com.otavio.data.analysis.entity;

public class Customer {

	private final String cnpj;
	private final String name;
	private final String businessArea;

	public Customer(final String cnpj, final String name, final String businessArea) {
		this.cnpj = cnpj;
		this.name = name;
		this.businessArea = businessArea;
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getName() {
		return name;
	}

	public String getBusinessArea() {
		return businessArea;
	}
}