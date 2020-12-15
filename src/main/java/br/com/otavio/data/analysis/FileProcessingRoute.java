package br.com.otavio.data.analysis;

import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.LoggingLevel.INFO;

import java.util.Set;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.apache.camel.support.builder.ValueBuilder;
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
import br.com.otavio.data.analysis.processor.GroupElementsProcessor;
import br.com.otavio.data.analysis.processor.ParseSingleLineProcessor;

@Component
public class FileProcessingRoute extends RouteBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingRoute.class);

	@Value("${app.allowed-file-extensions}")
	private Set<String> extensions;

	@Override
	public void configure() throws Exception {
		from("file:{{app.input-dir}}")
			.filter(new FileExtensionFilter(extensions))
			.log(INFO, LOGGER, "[${header.CamelFileName}] A new file has been received")
			.log(DEBUG, LOGGER, "[${header.CamelFileName}] File content is ${body}")

			.to("seda:asyncFileProcess");

		from("seda:asyncFileProcess?concurrentConsumers={{app.concurrent-consumers}}")
			.log(INFO, LOGGER, "Starting to process file: ${header.CamelFileName}")
			
			.split(bodyTokenizedByNewLines(), new GroupedBodyAggregationStrategy())
				.streaming().parallelProcessing().stopOnAggregateException()
				.process(new ParseSingleLineProcessor())
				.log(DEBUG, LOGGER, "Processing line ${body}")
			.end()

			.process(new GroupElementsProcessor())

			.process(new GetNumOfCustomersProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileName}] GetNumOfCustomers done with ${header.NUM_OF_CUSTOMERS}")

			.process(new GetNumOfSalesmenProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileName}] GetNumOfSalesmen done with ${header.NUM_OF_SALESMEN}")

			.process(new GetMostExpensiveSaleProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileName}] GetMostExpensiveSale done with ${header.MOST_EXPENSIVE_SALE}")

			.process(new GetWorstSalesmenProcessor())
			.log(DEBUG, LOGGER, "[${header.CamelFileName}] GetWorstSalesmen done with ${header.WORST_SALESMEN}")

			.process(new FormatOutputProcessor())
			.log(INFO, LOGGER, "[${header.CamelFileName}] The output has been created")

			.toD("file:{{app.output-dir}}?fileName=${file:name.noext}.done.dat")
			.log(INFO, LOGGER, "[${header.CamelFileName}] Done writing file to ${header.CamelFileNameProduced}")
			.log(DEBUG, LOGGER, "[${header.CamelFileName}] File content wrote is ${body}");
	}

	private ValueBuilder bodyTokenizedByNewLines() {
		return body().tokenize("\n");
	}
}