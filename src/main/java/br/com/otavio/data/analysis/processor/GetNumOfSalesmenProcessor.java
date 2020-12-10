package br.com.otavio.data.analysis.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.Headers;
import br.com.otavio.data.analysis.entity.InputFileData;

public class GetNumOfSalesmenProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		final InputFileData records = exchange.getIn().getBody(InputFileData.class);

		final int numOfSalesmen = records.getSalesmen().size();
		exchange.getIn().setHeader(Headers.NUM_OF_SALESMEN, numOfSalesmen);
	}
}