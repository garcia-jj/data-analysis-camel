package br.com.otavio.data.analysis.processor;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.otavio.data.analysis.Headers;

@ExtendWith(MockitoExtension.class)
public class FormatOutputProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;

	private Processor processor;

	@BeforeEach
	public void setup() {
		when(exchange.getIn()).thenReturn(in);
		processor = new FormatOutputProcessor();
	}

	@Test
	public void shouldFormatAppendingCustomersThenSalesmenThenExpensiveSaleThenWorstSalesman() throws Exception {
		doReturn(1).when(in).getHeader(Headers.NUM_OF_CUSTOMERS, Integer.class);
		doReturn(2).when(in).getHeader(Headers.NUM_OF_SALESMEN, Integer.class);
		doReturn("A").when(in).getHeader(Headers.MOST_EXPENSIVE_SALE, String.class);
		doReturn("B").when(in).getHeader(Headers.WORST_SALESMEN, String.class);

		processor.process(exchange);

		verify(in).setBody("1\u00E72\u00E7A\u00E7B");
	}
}