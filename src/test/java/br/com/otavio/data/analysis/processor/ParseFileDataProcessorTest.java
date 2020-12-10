package br.com.otavio.data.analysis.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

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
import br.com.otavio.data.analysis.entity.InputFileData;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.Salesman;

@ExtendWith(MockitoExtension.class)
public class ParseFileDataProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;
	private @Captor ArgumentCaptor<InputFileData> data;

	private Processor processor;

	@BeforeEach
	public void setup() throws Exception {
		when(exchange.getIn()).thenReturn(in);
		processor = new ParseFileDataProcessor();

		final URL filePath = getClass().getClassLoader().getResource("example-to-import.dat");
		doReturn(Paths.get(filePath.toURI()).toString()).when(in).getHeader(Exchange.FILE_PATH, String.class);
	}

	@Test
	public void shouldLoadCustomerEntries() throws Exception {
		processor.process(exchange);

		verify(in).setBody(data.capture());

		final List<Customer> customers = data.getValue().getCustomers();
		assertThat(customers).hasSize(2);

		assertThat(customers).first().satisfies(e -> {
			assertThat(e.getCnpj()).isEqualTo("2345675434544345");
			assertThat(e.getName()).isEqualTo("Jose da Silva");
			assertThat(e.getBusinessArea()).isEqualTo("Rural");
		});

		assertThat(customers).last().satisfies(e -> {
			assertThat(e.getCnpj()).isEqualTo("2345675433444345");
			assertThat(e.getName()).isEqualTo("Eduardo Pereira");
			assertThat(e.getBusinessArea()).isEqualTo("Rural");
		});
	}

	@Test
	public void shouldLoadSalesmanEntries() throws Exception {
		processor.process(exchange);

		verify(in).setBody(data.capture());

		final List<Salesman> salesmen = data.getValue().getSalesmen();
		assertThat(salesmen).hasSize(2);

		assertThat(salesmen).first().satisfies(e -> {
			assertThat(e.getCpf()).isEqualTo("1234567891234");
			assertThat(e.getName()).isEqualTo("Pedro");
			assertThat(e.getSalary()).isEqualByComparingTo(new BigDecimal("50000"));
		});

		assertThat(salesmen).last().satisfies(e -> {
			assertThat(e.getCpf()).isEqualTo("3245678865434");
			assertThat(e.getName()).isEqualTo("Paulo");
			assertThat(e.getSalary()).isEqualByComparingTo(new BigDecimal("40000.99"));
		});
	}

	@Test
	public void shouldLoadSalesEntries() throws Exception {
		processor.process(exchange);

		verify(in).setBody(data.capture());

		final List<Sale> sales = data.getValue().getSales();
		assertThat(sales).hasSize(2);

		assertThat(sales).first().satisfies(e -> {
			assertThat(e.getId()).isEqualTo("10");
			assertThat(e.getSalesmanName()).isEqualTo("Pedro");

			assertThat(e.getItems()).hasSize(3);
			assertThat(e.getItems()).element(0).satisfies(i -> {
				assertThat(i.getId()).isEqualTo("1");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("100"));
				assertThat(i.getQuantity()).isEqualTo(10);
			});

			assertThat(e.getItems()).element(1).satisfies(i -> {
				assertThat(i.getId()).isEqualTo("2");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("2.5"));
				assertThat(i.getQuantity()).isEqualTo(30);
			});

			assertThat(e.getItems()).element(2).satisfies(i -> {
				assertThat(i.getId()).isEqualTo("3");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("3.10"));
				assertThat(i.getQuantity()).isEqualTo(40);
			});
		});

		assertThat(sales).last().satisfies(e -> {
			assertThat(e.getId()).isEqualTo("08");
			assertThat(e.getSalesmanName()).isEqualTo("Paulo");

			assertThat(e.getItems()).hasSize(3);
			assertThat(e.getItems()).element(0).satisfies(i -> {
				assertThat(i.getId()).isEqualTo("1");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("10"));
				assertThat(i.getQuantity()).isEqualTo(34);
			});

			assertThat(e.getItems()).element(1).satisfies(i -> {
				assertThat(i.getId()).isEqualTo("2");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("1.5"));
				assertThat(i.getQuantity()).isEqualTo(33);
			});

			assertThat(e.getItems()).element(2).satisfies(i -> {
				assertThat(i.getId()).isEqualTo("3");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("0.1"));
				assertThat(i.getQuantity()).isEqualTo(40);
			});
		});
	}
}