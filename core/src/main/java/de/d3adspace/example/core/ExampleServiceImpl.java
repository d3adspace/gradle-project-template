package de.d3adspace.example.core;

import de.d3adspace.example.api.ExampleService;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class ExampleServiceImpl implements ExampleService {

    private final String response;

    public ExampleServiceImpl(String response) {
        this.response = response;
    }

    @Override
    public String fetchExampleData() {

        return response;
    }
}
