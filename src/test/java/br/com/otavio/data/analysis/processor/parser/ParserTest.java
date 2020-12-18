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
			assertThat(((Salesman) e).cpf()).isEqualTo("1234567891234");
			assertThat(((Salesman) e).name()).isEqualTo("Pedro");
			assertThat(((Salesman) e).salary()).isEqualByComparingTo(new BigDecimal("50000"));
		});
	}

	@Test
	public void shouldParse002AsCustomer() {
		final Object entity = ParserFactory.from("002").parse("002\u00E72345675434544345\u00E7Jose da Silva\u00E7Rural");

		assertThat(entity).satisfies(e -> {
			assertThat(e).isInstanceOf(Customer.class);
			assertThat(((Customer) e).cnpj()).isEqualTo("2345675434544345");
			assertThat(((Customer) e).name()).isEqualTo("Jose da Silva");
			assertThat(((Customer) e).businessArea()).isEqualTo("Rural");
		});
	}

	@Test
	public void shouldParse003AsSale() {
		final Object entity = ParserFactory.from("003").parse("003\u00E710\u00E7[1-10-100,2-30-2.50,3-40-3.10]\u00E7Pedro");

		assertThat(entity).satisfies(e -> {
			assertThat(e).isInstanceOf(Sale.class);
			assertThat(((Sale) e).id()).isEqualTo("10");
			assertThat(((Sale) e).salesmanName()).isEqualTo("Pedro");

			assertThat(((Sale) e).items()).element(0).satisfies(first -> {
				assertThat(first.id()).isEqualTo("1");
				assertThat(first.price()).isEqualByComparingTo(new BigDecimal("100"));
				assertThat(first.quantity()).isEqualTo(10);
			});

			assertThat(((Sale) e).items()).element(1).satisfies(second -> {
				assertThat(second.id()).isEqualTo("2");
				assertThat(second.price()).isEqualByComparingTo(new BigDecimal("2.5"));
				assertThat(second.quantity()).isEqualTo(30);
			});

			assertThat(((Sale) e).items()).element(2).satisfies(third -> {
				assertThat(third.id()).isEqualTo("3");
				assertThat(third.price()).isEqualByComparingTo(new BigDecimal("3.1"));
				assertThat(third.quantity()).isEqualTo(40);
			});
		});
	}
}