package br.com.otavio.data.analysis.processor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.Headers;
import br.com.otavio.data.analysis.entity.InputFileData;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.SaleItem;

public class GetMostExpensiveSaleProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		final InputFileData records = exchange.getIn().getBody(InputFileData.class);

		final String mostExpensiveSale = records.getSales().stream().max(bySaleAmount()).map(Sale::getId)
				.orElseThrow(() -> new NoSuchElementException("Unable to found the most expensive sale"));

		exchange.getIn().setHeader(Headers.MOST_EXPENSIVE_SALE, mostExpensiveSale);
	}

	public static final Comparator<Sale> bySaleAmount() {
		return (o1, o2) -> {
			final BigDecimal amount1 = o1.getItems().stream().map(sumItems()).reduce(BigDecimal.ZERO, BigDecimal::add);
			final BigDecimal amount2 = o2.getItems().stream().map(sumItems()).reduce(BigDecimal.ZERO, BigDecimal::add);

			return amount1.compareTo(amount2);
		};
	}

	private static Function<SaleItem, BigDecimal> sumItems() {
		return $ -> $.getPrice().multiply(BigDecimal.valueOf($.getQuantity()));
	}
}