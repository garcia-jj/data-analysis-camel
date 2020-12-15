package br.com.otavio.data.analysis.processor;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.otavio.data.analysis.entity.Customer;
import br.com.otavio.data.analysis.entity.GroupedData;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.SaleItem;
import br.com.otavio.data.analysis.entity.Salesman;

@ExtendWith(MockitoExtension.class)
public class GroupElementsProcessorTest {

	private @Mock Exchange exchange;
	private @Mock Message in;
	private @Captor ArgumentCaptor<GroupedData> data;

	private Processor processor;

	@BeforeEach
	public void setup() throws Exception {
		when(exchange.getIn()).thenReturn(in);
		processor = new GroupElementsProcessor();

		List<Object> records = List.of(
				new Salesman("1234A", "Salesman 1", new BigDecimal("1")),
				new Salesman("5678A", "Salesman 2", new BigDecimal("2")), new Customer("1234B", "Customer 1", "A"),
				new Customer("5678B", "Customer 2", "B"),
				new Sale("10", List.of(new SaleItem("1", 10, new BigDecimal("100"))), "Pedro"),
				new Sale("08", List.of(new SaleItem("2", 20, new BigDecimal("200"))), "Paulo")
		);

		when(in.getBody(List.class)).thenReturn(records);
	}

	@Test
	public void shouldLoadCustomerEntries() throws Exception {
		processor.process(exchange);

		verify(in).setBody(data.capture());

		final List<Customer> customers = data.getValue().getCustomers();
		assertThat(customers).hasSize(2);

		assertThat(customers).first().satisfies(e -> {
			assertThat(e.getCnpj()).isEqualTo("1234B");
			assertThat(e.getName()).isEqualTo("Customer 1");
			assertThat(e.getBusinessArea()).isEqualTo("A");
		});

		assertThat(customers).last().satisfies(e -> {
			assertThat(e.getCnpj()).isEqualTo("5678B");
			assertThat(e.getName()).isEqualTo("Customer 2");
			assertThat(e.getBusinessArea()).isEqualTo("B");
		});
	}

	@Test
	public void shouldLoadSalesmanEntries() throws Exception {
		processor.process(exchange);

		verify(in).setBody(data.capture());

		final List<Salesman> salesmen = data.getValue().getSalesmen();
		assertThat(salesmen).hasSize(2);

		assertThat(salesmen).first().satisfies(e -> {
			assertThat(e.getCpf()).isEqualTo("1234A");
			assertThat(e.getName()).isEqualTo("Salesman 1");
			assertThat(e.getSalary()).isEqualByComparingTo(new BigDecimal("1"));
		});

		assertThat(salesmen).last().satisfies(e -> {
			assertThat(e.getCpf()).isEqualTo("5678A");
			assertThat(e.getName()).isEqualTo("Salesman 2");
			assertThat(e.getSalary()).isEqualByComparingTo(new BigDecimal("2"));
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

			assertThat(e.getItems()).hasSize(1);
			assertThat(e.getItems()).first().satisfies(i -> {
				assertThat(i.getId()).isEqualTo("1");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("100"));
				assertThat(i.getQuantity()).isEqualTo(10);
			});

		});
		assertThat(sales).last().satisfies(e -> {
			assertThat(e.getId()).isEqualTo("08");
			assertThat(e.getSalesmanName()).isEqualTo("Paulo");

			assertThat(e.getItems()).hasSize(1);
			assertThat(e.getItems()).first().satisfies(i -> {
				assertThat(i.getId()).isEqualTo("2");
				assertThat(i.getPrice()).isEqualByComparingTo(new BigDecimal("200"));
				assertThat(i.getQuantity()).isEqualTo(20);
			});
		});
	}
}