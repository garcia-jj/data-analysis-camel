package br.com.otavio.data.analysis.processor.parser;

import br.com.otavio.data.analysis.entity.Customer;

public class CustomerParser extends AbstractLineParser implements Parser<Customer> {

	@Override
	public Customer parse(final String line) {
		final String[] pairs = parseLine(line);
		return new Customer(pairs[1], pairs[2], pairs[3]);
	}
}