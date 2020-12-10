package br.com.otavio.data.analysis.processor.parser;

import java.math.BigDecimal;

import br.com.otavio.data.analysis.entity.Salesman;

public class SalesmanParser implements Parser<Salesman> {

	private static final String RECORD_DELIMITER = "\u00E7";

	@Override
	public Salesman parse(final String line) {
		final String[] pairs = line.split(RECORD_DELIMITER);
		return new Salesman(pairs[1], pairs[2], new BigDecimal(pairs[3]));
	}
}