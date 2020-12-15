package br.com.otavio.data.analysis.processor;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.entity.Customer;
import br.com.otavio.data.analysis.entity.GroupedData;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.Salesman;

public class GroupElementsProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		final List<?> elements = exchange.getIn().getBody(List.class);

		final List<Customer> customers = elements.stream().filter(isCustomer()).map(toCustomer()).collect(toList());
		final List<Sale> sales = elements.stream().filter(isSale()).map(toSale()).collect(toList());
		final List<Salesman> salesmen = elements.stream().filter(isSalesman()).map(toSalesman()).collect(toList());

		exchange.getIn().setBody(new GroupedData(customers, sales, salesmen));
	}

	private Predicate<Object> isCustomer() {
		return Customer.class::isInstance;
	}

	private Function<Object, Customer> toCustomer() {
		return Customer.class::cast;
	}

	private Predicate<Object> isSale() {
		return Sale.class::isInstance;
	}

	private Function<Object, Sale> toSale() {
		return Sale.class::cast;
	}

	private Predicate<Object> isSalesman() {
		return Salesman.class::isInstance;
	}

	private Function<Object, Salesman> toSalesman() {
		return Salesman.class::cast;
	}
}