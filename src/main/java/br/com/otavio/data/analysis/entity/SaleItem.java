package br.com.otavio.data.analysis.entity;

import java.math.BigDecimal;

public record SaleItem(String id, int quantity, BigDecimal price) {

}