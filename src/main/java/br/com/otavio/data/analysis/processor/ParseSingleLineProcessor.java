package br.com.otavio.data.analysis.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.processor.parser.Parser;

public class ParseSingleLineProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		final String line = exchange.getIn().getBody(String.class).trim();
		final String recordType = line.substring(0, 3);
		final Object record = Parser.from(recordType).parse(line);

		exchange.getIn().setBody(record);
	}
}