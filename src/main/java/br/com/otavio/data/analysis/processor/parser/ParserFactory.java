package br.com.otavio.data.analysis.processor.parser;

import java.util.Objects;

public final class ParserFactory {

	public static <T> Parser<?> from(final String type) {
		if (Objects.equals(type, "001"))
			return new SalesmanParser();

		if (Objects.equals(type, "002"))
			return new CustomerParser();

		if (Objects.equals(type, "003"))
			return new SaleParser();

		throw new IllegalArgumentException("Unknow type: " + type);
	}
}