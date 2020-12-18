package br.com.otavio.data.analysis.processor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.otavio.data.analysis.Headers;
import br.com.otavio.data.analysis.entity.Customer;
import br.com.otavio.data.analysis.entity.GroupedData;

@ExtendWith(MockitoExtension.class)
public class GetNumOfCustomersProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;

	private Processor processor;

	@BeforeEach
	public void setup() {
		final Customer customer1 = new Customer("A", "B", "C");
		final Customer customer2 = new Customer("D", "E", "F");

		when(in.getBody(GroupedData.class)).thenReturn(new GroupedData(List.of(customer1, customer2), List.of(), List.of()));

		when(exchange.getIn()).thenReturn(in);
		processor = new GetNumOfCustomersProcessor();
	}

	@Test
	public void shouldSetResultIntoHeader() throws Exception {
		processor.process(exchange);

		verify(in).setHeader(Headers.NUM_OF_CUSTOMERS, 2);
	}
}