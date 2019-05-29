package de.d3adspace.example.core;

import de.d3adspace.example.api.ExampleApplication;
import de.d3adspace.example.api.ExampleService;

import java.io.PrintStream;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class ExampleApplicationImpl implements ExampleApplication {

    private final PrintStream printStream;
    private final ExampleService exampleService;

    public ExampleApplicationImpl(PrintStream printStream, ExampleService exampleService) {
        this.printStream = printStream;
        this.exampleService = exampleService;
    }

    @Override
    public void run() {
        String exampleData = exampleService.fetchExampleData();
        printStream.println(exampleData);
    }
}
