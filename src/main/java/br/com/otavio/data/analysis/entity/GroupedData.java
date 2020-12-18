package br.com.otavio.data.analysis.entity;

import java.util.List;

public record GroupedData(List<Customer> customers, List<Sale> sales, List<Salesman> salesmen) {

}