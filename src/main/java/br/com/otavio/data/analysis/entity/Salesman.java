package br.com.otavio.data.analysis.entity;

import java.math.BigDecimal;

public class Salesman {

	private final String cpf;
	private final String name;
	private final BigDecimal salary;

	public Salesman(final String cpf, final String name, final BigDecimal salary) {
		this.cpf = cpf;
		this.name = name;
		this.salary = salary;
	}

	public String getCpf() {
		return cpf;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getSalary() {
		return salary;
	}
}