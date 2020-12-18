package br.com.otavio.data.analysis.processor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import br.com.otavio.data.analysis.Headers;
import br.com.otavio.data.analysis.entity.GroupedData;
import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.SaleItem;

public class GetWorstSalesmenProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		final GroupedData records = exchange.getIn().getBody(GroupedData.class);
		final Map<String, BigDecimal> salesmanSales = new LinkedHashMap<>();

		for (final Sale sale : records.sales()) {
			final BigDecimal val = sale.items().stream().map(sumItems()).reduce(BigDecimal.ZERO, BigDecimal::add);
			salesmanSales.compute(sale.salesmanName(), (k, v) -> v == null ? val : v.add(val));
		}

		final Entry<String, BigDecimal> worstSalesman = salesmanSales.entrySet().stream().min(byMapValue())
				.orElseThrow(() -> new NoSuchElementException("Unable to found the worst salesman"));

		exchange.getIn().setHeader(Headers.WORST_SALESMEN, worstSalesman.getKey());
	}

	private Comparator<Entry<String, BigDecimal>> byMapValue() {
		return (x, y) -> x.getValue().compareTo(y.getValue());
	}

	private static Function<SaleItem, BigDecimal> sumItems() {
		return $ -> $.price().multiply(BigDecimal.valueOf($.quantity()));
	}
}