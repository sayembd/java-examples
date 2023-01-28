package com.sayemahmed.example.forkjoin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import org.junit.jupiter.api.Test;

public class ListSummerTest {

  @Test
  public void shouldSumEmptyList() {
    ListSummer summer = new ListSummer(List.of());
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    forkJoinPool.submit(summer);

    int result = summer.join();

    assertThat(result).isZero();
  }

  @Test
  public void shouldSumListWithOneElement() {
    ListSummer summer = new ListSummer(List.of(5));
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    forkJoinPool.submit(summer);

    int result = summer.join();

    assertThat(result).isEqualTo(5);
  }

  @Test
  public void shouldSumListWithMultipleElements() {
    ListSummer summer = new ListSummer(List.of(
        1, 2, 3, 4, 5, 6, 7, 8, 9
    ));
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    forkJoinPool.submit(summer);

    int result = summer.join();

    assertThat(result).isEqualTo(45);
  }
}