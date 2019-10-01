package de.d3adspace.example.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
class ExampleServiceImplTest {

  private static final String TEST_DATA = "resp";
  private ExampleServiceImpl exampleService;

  @BeforeEach
  void setUp() {

    exampleService = new ExampleServiceImpl(TEST_DATA);
  }

  @Test
  void testFetchBackendData() {
    String fetchedData = exampleService.fetchExampleData();

    assertEquals(TEST_DATA, fetchedData);
  }
}