package br.com.otavio.data.analysis.processor.parser;

import java.math.BigDecimal;

import br.com.otavio.data.analysis.entity.Salesman;

public class SalesmanParser extends AbstractLineParser implements Parser<Salesman> {

	@Override
	public Salesman parse(final String line) {
		final String[] pairs = parseLine(line);
		return new Salesman(pairs[1], pairs[2], new BigDecimal(pairs[3]));
	}
}