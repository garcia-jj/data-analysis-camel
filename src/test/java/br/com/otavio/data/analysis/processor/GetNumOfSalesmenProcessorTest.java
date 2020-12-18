package br.com.otavio.data.analysis.processor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import br.com.otavio.data.analysis.entity.GroupedData;
import br.com.otavio.data.analysis.entity.Salesman;

@ExtendWith(MockitoExtension.class)
public class GetNumOfSalesmenProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;

	private Processor processor;

	@BeforeEach
	public void setup() {
		final Salesman s1 = new Salesman("A", "B", BigDecimal.ONE);
		final Salesman s2 = new Salesman("D", "E", BigDecimal.ONE);
		final Salesman s3 = new Salesman("G", "H", BigDecimal.ONE);

		when(in.getBody(GroupedData.class)).thenReturn(new GroupedData(List.of(), List.of(), List.of(s1, s2, s3)));

		when(exchange.getIn()).thenReturn(in);
		processor = new GetNumOfSalesmenProcessor();
	}

	@Test
	public void shouldSetResultIntoHeader() throws Exception {
		processor.process(exchange);

		verify(in).setHeader(Headers.NUM_OF_SALESMEN, 3);
	}
}