package br.com.otavio.data.analysis.processor.parser;

public interface Parser<T> {

	T parse(String line);
}