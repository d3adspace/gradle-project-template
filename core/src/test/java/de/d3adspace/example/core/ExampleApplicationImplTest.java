package de.d3adspace.example.core;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
@ExtendWith(MockitoExtension.class)
class ExampleApplicationImplTest {

  @Mock
  private ExampleServiceImpl exampleService;
  @Mock
  private PrintStream printStream;

  private ExampleApplicationImpl exampleApplication;

  @BeforeEach
  void setUp() {

    exampleApplication = new ExampleApplicationImpl(printStream,
      exampleService);
  }

  @Test
  void testRun() {

    // Given
    String testData = "test";
    when(exampleService.fetchExampleData()).thenReturn(testData);

    // When
    exampleApplication.run();

    // Then
    verify(printStream).println(testData);
  }
}