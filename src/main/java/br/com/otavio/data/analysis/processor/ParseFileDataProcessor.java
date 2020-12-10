package br.com.otavio.data.analysis.processor;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.entity.Customer;
import br.com.otavio.data.analysis.entity.InputFileData;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.Salesman;
import br.com.otavio.data.analysis.processor.parser.Parser;

public class ParseFileDataProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		final String filePath = exchange.getIn().getHeader(Exchange.FILE_PATH, String.class);
		final Path file = Paths.get(filePath);

		final List<Object> entities = Files.lines(file, ISO_8859_1).map(toEntity()).collect(toList());

		final List<Customer> customers = entities.stream().filter(isCustomer()).map(toCustomer()).collect(toList());
		final List<Sale> sales = entities.stream().filter(isSale()).map(toSale()).collect(toList());
		final List<Salesman> salesmen = entities.stream().filter(isSalesman()).map(toSalesman()).collect(toList());

		exchange.getIn().setBody(new InputFileData(customers, sales, salesmen));
	}

	private Function<String, Object> toEntity() {
		return line -> Parser.from(line.substring(0, 3)).parse(line);
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