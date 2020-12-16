package br.com.otavio.data.analysis.processor.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.otavio.data.analysis.entity.Customer;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.Salesman;

public class ParserTest {

	@Test
	public void shouldFailWhenLineHasntBeenRecognized() {
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			ParserFactory.from("UNKNOW");
		});

		assertThat(exception.getMessage()).isEqualTo("Unknow type: UNKNOW");
	}

	@Test
	public void shouldFailWhenLineHasntBeenRecognizedNullSafe() {
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			ParserFactory.from(null);
		});

		assertThat(exception.getMessage()).isEqualTo("Unknow type: null");
	}

	@Test
	public void shouldParse001AsSalesman() {
		final Object entity = ParserFactory.from("001").parse("001\u00E71234567891234\u00E7Pedro\u00E750000");

		assertThat(entity).satisfies(e -> {
			assertThat(e).isInstanceOf(Salesman.class);
			assertThat(((Salesman) e).getCpf()).isEqualTo("1234567891234");
			assertThat(((Salesman) e).getName()).isEqualTo("Pedro");
			assertThat(((Salesman) e).getSalary()).isEqualByComparingTo(new BigDecimal("50000"));
		});
	}

	@Test
	public void shouldParse002AsCustomer() {
		final Object entity = ParserFactory.from("002").parse("002\u00E72345675434544345\u00E7Jose da Silva\u00E7Rural");

		assertThat(entity).satisfies(e -> {
			assertThat(e).isInstanceOf(Customer.class);
			assertThat(((Customer) e).getCnpj()).isEqualTo("2345675434544345");
			assertThat(((Customer) e).getName()).isEqualTo("Jose da Silva");
			assertThat(((Customer) e).getBusinessArea()).isEqualTo("Rural");
		});
	}

	@Test
	public void shouldParse003AsSale() {
		final Object entity = ParserFactory.from("003").parse("003\u00E710\u00E7[1-10-100,2-30-2.50,3-40-3.10]\u00E7Pedro");

		assertThat(entity).satisfies(e -> {
			assertThat(e).isInstanceOf(Sale.class);
			assertThat(((Sale) e).getId()).isEqualTo("10");
			assertThat(((Sale) e).getSalesmanName()).isEqualTo("Pedro");

			assertThat(((Sale) e).getItems()).element(0).satisfies(first -> {
				assertThat(first.getId()).isEqualTo("1");
				assertThat(first.getPrice()).isEqualByComparingTo(new BigDecimal("100"));
				assertThat(first.getQuantity()).isEqualTo(10);
			});

			assertThat(((Sale) e).getItems()).element(1).satisfies(second -> {
				assertThat(second.getId()).isEqualTo("2");
				assertThat(second.getPrice()).isEqualByComparingTo(new BigDecimal("2.5"));
				assertThat(second.getQuantity()).isEqualTo(30);
			});

			assertThat(((Sale) e).getItems()).element(2).satisfies(third -> {
				assertThat(third.getId()).isEqualTo("3");
				assertThat(third.getPrice()).isEqualByComparingTo(new BigDecimal("3.1"));
				assertThat(third.getQuantity()).isEqualTo(40);
			});
		});
	}
}