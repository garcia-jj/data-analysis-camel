package br.com.otavio.data.analysis.processor.parser;

class AbstractLineParser {

	private static final String RECORD_DELIMITER = "\u00E7";

	String[] parseLine(final String line) {
		return line.split(RECORD_DELIMITER);
	}
}