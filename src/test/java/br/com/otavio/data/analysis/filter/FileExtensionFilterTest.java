package br.com.otavio.data.analysis.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileExtensionFilterTest {

	private @Mock Exchange exchange;
	private @Mock Message in;

	private Predicate filter;

	@BeforeEach
	public void setup() {
		when(exchange.getIn()).thenReturn(in);
		filter = new FileExtensionFilter(Set.of("dat"));
	}

	@Test
	public void shouldIgnoreFilesWithoutExtension() {
		when(in.getHeader(Exchange.FILE_PATH, String.class)).thenReturn("file-without-extension");

		final boolean accepted = filter.matches(exchange);
		assertThat(accepted).isFalse();
	}

	@Test
	public void shouldIgnoreFilesWhenExceptionIsNotAllowed() {
		when(in.getHeader(Exchange.FILE_PATH, String.class)).thenReturn("file.txt");

		final boolean accepted = filter.matches(exchange);
		assertThat(accepted).isFalse();
	}

	@Test
	public void shouldAcceptFilesWhenExceptionIsAllowed() {
		when(in.getHeader(Exchange.FILE_PATH, String.class)).thenReturn("file.dat");

		final boolean accepted = filter.matches(exchange);
		assertThat(accepted).isTrue();
	}
}