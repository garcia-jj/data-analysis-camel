package br.com.otavio.data.analysis.entity;

import java.util.List;

public record Sale(String id, List<SaleItem> items, String salesmanName) {
}