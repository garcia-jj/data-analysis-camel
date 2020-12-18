package br.com.otavio.data.analysis.processor.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
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
public class GroupedDataBodyAggregationTest {

	private @Mock Exchange exchange;
	private @Mock Message in;
	private @Captor ArgumentCaptor<GroupedData> data;

	private GroupedDataBodyAggregation aggregation;

	@BeforeEach
	public void setup() throws Exception {
		when(exchange.getIn()).thenReturn(in);
		aggregation = new GroupedDataBodyAggregation();

		List<Object> records = List.of(new Salesman("1234A", "Salesman 1", new BigDecimal("1")),
				new Salesman("5678A", "Salesman 2", new BigDecimal("2")), new Customer("1234B", "Customer 1", "A"),
				new Customer("5678B", "Customer 2", "B"),
				new Sale("10", List.of(new SaleItem("1", 10, new BigDecimal("100"))), "Pedro"),
				new Sale("08", List.of(new SaleItem("2", 20, new BigDecimal("200"))), "Paulo"));

		when(in.getBody(List.class)).thenReturn(records);
	}

	@Test
	public void shouldLoadCustomerEntries() throws Exception {
		aggregation.doAggregate(exchange);

		verify(in).setBody(data.capture());

		final List<Customer> customers = data.getValue().customers();
		assertThat(customers).hasSize(2);

		assertThat(customers).first().satisfies(e -> {
			assertThat(e.cnpj()).isEqualTo("1234B");
			assertThat(e.name()).isEqualTo("Customer 1");
			assertThat(e.businessArea()).isEqualTo("A");
		});

		assertThat(customers).last().satisfies(e -> {
			assertThat(e.cnpj()).isEqualTo("5678B");
			assertThat(e.name()).isEqualTo("Customer 2");
			assertThat(e.businessArea()).isEqualTo("B");
		});
	}

	@Test
	public void shouldLoadSalesmanEntries() throws Exception {
		aggregation.doAggregate(exchange);

		verify(in).setBody(data.capture());

		final List<Salesman> salesmen = data.getValue().salesmen();
		assertThat(salesmen).hasSize(2);

		assertThat(salesmen).first().satisfies(e -> {
			assertThat(e.cpf()).isEqualTo("1234A");
			assertThat(e.name()).isEqualTo("Salesman 1");
			assertThat(e.salary()).isEqualByComparingTo(new BigDecimal("1"));
		});

		assertThat(salesmen).last().satisfies(e -> {
			assertThat(e.cpf()).isEqualTo("5678A");
			assertThat(e.name()).isEqualTo("Salesman 2");
			assertThat(e.salary()).isEqualByComparingTo(new BigDecimal("2"));
		});
	}

	@Test
	public void shouldLoadSalesEntries() throws Exception {
		aggregation.doAggregate(exchange);

		verify(in).setBody(data.capture());

		final List<Sale> sales = data.getValue().sales();
		assertThat(sales).hasSize(2);

		assertThat(sales).first().satisfies(e -> {
			assertThat(e.id()).isEqualTo("10");
			assertThat(e.salesmanName()).isEqualTo("Pedro");

			assertThat(e.items()).hasSize(1);
			assertThat(e.items()).first().satisfies(i -> {
				assertThat(i.id()).isEqualTo("1");
				assertThat(i.price()).isEqualByComparingTo(new BigDecimal("100"));
				assertThat(i.quantity()).isEqualTo(10);
			});

		});
		assertThat(sales).last().satisfies(e -> {
			assertThat(e.id()).isEqualTo("08");
			assertThat(e.salesmanName()).isEqualTo("Paulo");

			assertThat(e.items()).hasSize(1);
			assertThat(e.items()).first().satisfies(i -> {
				assertThat(i.id()).isEqualTo("2");
				assertThat(i.price()).isEqualByComparingTo(new BigDecimal("200"));
				assertThat(i.quantity()).isEqualTo(20);
			});
		});
	}
}