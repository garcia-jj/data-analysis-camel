package br.com.otavio.data.analysis.processor.parser;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import br.com.otavio.data.analysis.entity.Sale;
import br.com.otavio.data.analysis.entity.SaleItem;

public class SaleParser extends AbstractLineParser implements Parser<Sale> {

	private static final String ITEM_DELIMITER = ",";
	private static final String ITEM_COLUMN_DELIMITER = "-";

	@Override
	public Sale parse(final String line) {
		final String[] pairs = parseLine(line);
		final List<SaleItem> items = parseItems(pairs[2]);
		return new Sale(pairs[1], items, pairs[3]);
	}

	private List<SaleItem> parseItems(final String str) {
		final String[] pairs = str.substring(1, str.length() - 1).split(ITEM_DELIMITER);
		return Stream.of(pairs).map(toItemPairs()).map(toItem()).collect(toList());
	}

	private Function<String, String[]> toItemPairs() {
		return e -> e.split(ITEM_COLUMN_DELIMITER);
	}

	private Function<String[], SaleItem> toItem() {
		return v -> new SaleItem(v[0], Integer.parseInt(v[1]), new BigDecimal(v[2]));
	}
}