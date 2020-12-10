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
import br.com.otavio.data.analysis.entity.InputFileData;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.SaleItem;

@ExtendWith(MockitoExtension.class)
public class GetWorstSalesmanProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;
	private @Mock InputFileData data;

	private Processor processor;

	@BeforeEach
	public void setup() {
		when(exchange.getIn()).thenReturn(in);
		when(in.getBody(InputFileData.class)).thenReturn(data);
		processor = new GetWorstSalesmenProcessor();
	}

	@Test
	public void shouldSetResultIntoHeader() throws Exception {
		final Sale sale1 = new Sale("A", List.of(new SaleItem("A0", 1, new BigDecimal("5")), new SaleItem("A2", 2, new BigDecimal("10"))), "Franklin");
		final Sale sale2 = new Sale("B", List.of(new SaleItem("A0", 1, new BigDecimal("5"))), "Theodore");
		when(data.getSales()).thenReturn(List.of(sale1, sale2));

		processor.process(exchange);

		verify(in).setHeader(Headers.WORST_SALESMEN, "Theodore");
	}
}