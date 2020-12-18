package br.com.otavio.data.analysis.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.Headers;
import br.com.otavio.data.analysis.entity.GroupedData;

public class GetNumOfCustomersProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		final GroupedData records = exchange.getIn().getBody(GroupedData.class);

		final int numOfCustomers = records.customers().size();
		exchange.getIn().setHeader(Headers.NUM_OF_CUSTOMERS, numOfCustomers);
	}
}