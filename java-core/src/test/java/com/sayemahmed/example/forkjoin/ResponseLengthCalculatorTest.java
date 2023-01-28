package com.sayemahmed.example.forkjoin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import org.junit.jupiter.api.Test;

public class ResponseLengthCalculatorTest {

  @Test
  public void shouldReturnEmptyMapForEmptyList() {
    ResponseLengthCalculator responseLengthCalculator = new ResponseLengthCalculator(Collections.emptyList());
    ForkJoinPool pool = new ForkJoinPool();

    pool.submit(responseLengthCalculator);

    Map<String, Integer> result = responseLengthCalculator.join();
    assertThat(result).isEmpty();
  }

  @Test
  public void shouldHandle200Ok() {
    ResponseLengthCalculator responseLengthCalculator = new ResponseLengthCalculator(List.of(
        "http://httpstat.us/200"
    ));
    ForkJoinPool pool = new ForkJoinPool();

    pool.submit(responseLengthCalculator);

    Map<String, Integer> result = responseLengthCalculator.join();
    assertThat(result)
        .hasSize(1)
        .containsKeys("http://httpstat.us/200")
        .containsValue(6);
  }

  @Test
  public void shouldFetchResponseForDifferentResponseStatus() {
    ResponseLengthCalculator responseLengthCalculator = new ResponseLengthCalculator(List.of(
        "http://httpstat.us/200",
        "http://httpstat.us/302",
        "http://httpstat.us/404",
        "http://httpstat.us/502"
    ));
    ForkJoinPool pool = new ForkJoinPool();

    pool.submit(responseLengthCalculator);

    Map<String, Integer> result = responseLengthCalculator.join();
    assertThat(result)
        .hasSize(4);
  }
}