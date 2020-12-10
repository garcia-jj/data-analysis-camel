package br.com.otavio.data.analysis.processor.parser;

import br.com.otavio.data.analysis.entity.Customer;

public class CustomerParser implements Parser<Customer> {

	private static final String RECORD_DELIMITER = "\u00E7";

	@Override
	public Customer parse(final String line) {
		final String[] pairs = line.split(RECORD_DELIMITER);
		return new Customer(pairs[1], pairs[2], pairs[3]);
	}
}