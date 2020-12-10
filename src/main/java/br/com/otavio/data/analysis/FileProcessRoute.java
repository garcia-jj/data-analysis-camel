package br.com.otavio.data.analysis;

import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.LoggingLevel.INFO;

import java.util.Set;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.otavio.data.analysis.filter.FileExtensionFilter;
import br.com.otavio.data.analysis.processor.FormatOutputProcessor;
import br.com.otavio.data.analysis.processor.GetMostExpensiveSaleProcessor;
import br.com.otavio.data.analysis.processor.GetNumOfCustomersProcessor;
import br.com.otavio.data.analysis.processor.GetNumOfSalesmenProcessor;
import br.com.otavio.data.analysis.processor.GetWorstSalesmenProcessor;
import br.com.otavio.data.analysis.processor.ParseFileDataProcessor;

@Component
public class FileProcessRoute extends RouteBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessRoute.class);

	@Value("${app.allowed-file-extensions}")
	private Set<String> extensions;

	@Override
	public void configure() throws Exception {
		from("file:{{app.input-dir}}")
			.filter(new FileExtensionFilter(extensions))

			.log(INFO, LOGGER, "[${header.CamelFileAbsolutePath}] A new file has been received")
			.log(DEBUG, LOGGER, "[${header.CamelFileAbsolutePath}] File content is ${body}")
			.to("seda:asyncFileProcess");

		from("seda:asyncFileProcess?concurrentConsumers={{app.concurrent-consumers}}")
			.process(new ParseFileDataProcessor())
			.log(INFO, LOGGER, "[${header.CamelFileAbsolutePath}] The file has been parsed")

			.process(new GetNumOfCustomersProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileAbsolutePath}] GetNumOfCustomers done with ${header.NUM_OF_CUSTOMERS}")

			.process(new GetNumOfSalesmenProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileAbsolutePath}] GetNumOfSalesmen done with ${header.NUM_OF_SALESMEN}")

			.process(new GetMostExpensiveSaleProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileAbsolutePath}] GetMostExpensiveSale done with ${header.MOST_EXPENSIVE_SALE}")

			.process(new GetWorstSalesmenProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileAbsolutePath}] GetWorstSalesmen done with ${header.WORST_SALESMEN}")

			.process(new FormatOutputProcessor())
			.log(INFO, LOGGER, "[${header.CamelFileAbsolutePath}] The output has been created")

			.toD("file:{{app.output-dir}}?fileName=${file:name.noext}.done.dat")
			.log(INFO, LOGGER, "[${header.CamelFileAbsolutePath}] Done writing file to ${header.CamelFileNameProduced}")
			.log(DEBUG, LOGGER, "[${header.CamelFileAbsolutePath}] File content wrote is ${body}");
	}
}