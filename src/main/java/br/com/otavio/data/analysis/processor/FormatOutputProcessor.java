package br.com.otavio.data.analysis.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.Headers;

public class FormatOutputProcessor implements Processor {

	private static final String LINE_FORMAT = "%d\u00E7%d\u00E7%s\u00E7%s";

	@Override
	public void process(final Exchange exchange) throws Exception {
		final Message in = exchange.getIn();

		final int numOfCustomers = in.getHeader(Headers.NUM_OF_CUSTOMERS, Integer.class);
		final int numOfSalesmen = in.getHeader(Headers.NUM_OF_SALESMEN, Integer.class);
		final String mostExpensiveSale = in.getHeader(Headers.MOST_EXPENSIVE_SALE, String.class);
		final String worstSalesman = in.getHeader(Headers.WORST_SALESMEN, String.class);

		final String line = formatLine(numOfCustomers, numOfSalesmen, mostExpensiveSale, worstSalesman);
		in.setBody(line);
	}

	private String formatLine(final int numOfCustomers, final int numOfSalesmen, final String mostExpensiveSale,
			final String worstSalesman) {
		return String.format(LINE_FORMAT, numOfCustomers, numOfSalesmen, mostExpensiveSale, worstSalesman);
	}
}