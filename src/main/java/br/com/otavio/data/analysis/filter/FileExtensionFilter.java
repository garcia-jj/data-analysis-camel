package br.com.otavio.data.analysis.filter;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class FileExtensionFilter implements Predicate {

	private final Set<String> extensions;

	public FileExtensionFilter(final Set<String> extensions) {
		this.extensions = extensions;
	}

	@Override
	public boolean matches(final Exchange exchange) {
		final String filePath = exchange.getIn().getHeader(Exchange.FILE_PATH, String.class);
		final String fileExtension = filePath.contains(".") ? filePath.substring(filePath.lastIndexOf(".") + 1) : "";

		return extensions.contains(fileExtension);
	}
}