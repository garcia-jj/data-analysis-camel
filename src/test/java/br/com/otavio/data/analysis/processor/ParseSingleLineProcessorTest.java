package br.com.otavio.data.analysis.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.otavio.data.analysis.entity.Customer;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.Salesman;

@ExtendWith(MockitoExtension.class)
public class ParseSingleLineProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;
	private @Captor ArgumentCaptor<Object> record;

	private Processor processor;

	@BeforeEach
	public void setup() {
		when(exchange.getIn()).thenReturn(in);
		processor = new ParseSingleLineProcessor();
	}

	@Test
	public void parseLinesStartingWith001() throws Exception {
		when(in.getBody(String.class)).thenReturn("001ç1234567891234çJoãoç50000");
		processor.process(exchange);

		verify(in).setBody(record.capture());
		assertThat(record.getValue()).isInstanceOf(Salesman.class);
	}

	@Test
	public void parseLinesStartingWith002() throws Exception {
		when(in.getBody(String.class)).thenReturn("002ç2345675434544345çJose SilvaçRural");
		processor.process(exchange);

		verify(in).setBody(record.capture());
		assertThat(record.getValue()).isInstanceOf(Customer.class);
	}

	@Test
	public void parseLinesStartingWith003() throws Exception {
		when(in.getBody(String.class)).thenReturn("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çJoão");
		processor.process(exchange);

		verify(in).setBody(record.capture());
		assertThat(record.getValue()).isInstanceOf(Sale.class);
	}
}
