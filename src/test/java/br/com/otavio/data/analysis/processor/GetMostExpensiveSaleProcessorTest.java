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
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.SaleItem;

@ExtendWith(MockitoExtension.class)
public class GetMostExpensiveSaleProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;

	private Processor processor;

	@BeforeEach
	public void setup() {
		when(exchange.getIn()).thenReturn(in);

		final Sale sale1 = new Sale("A", List.of(new SaleItem("A0", 1, new BigDecimal("5")), new SaleItem("A2", 2, new BigDecimal("10"))), "Franklin");
		final Sale sale2 = new Sale("B", List.of(new SaleItem("A0", 1, new BigDecimal("5"))), "Theodore");
		when(in.getBody(GroupedData.class)).thenReturn(new GroupedData(List.of(), List.of(sale1, sale2), List.of()));

		processor = new GetMostExpensiveSaleProcessor();
	}

	@Test
	public void shouldSetResultIntoHeader() throws Exception {
		processor.process(exchange);
		verify(in).setHeader(Headers.MOST_EXPENSIVE_SALE, "A");
	}
}