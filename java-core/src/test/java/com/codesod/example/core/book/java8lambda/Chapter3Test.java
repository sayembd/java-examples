/*
 * Copyright 2017 MD Sayem Ahmed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codesod.example.core.book.java8lambda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author MD Sayem Ahmed
 */
public class Chapter3Test {

  @Test
  public void givenNullInputArray_whenSumArrayUsingReduce_thenExceptionThrown() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> Chapter3.sumArrayUsingReduce(null));
  }

  @Test
  public void givenEmptyInputArray_whenSumArrayUsingReduce_thenZeroReturned() {
    int[] inputArray = {};

    int sum = Chapter3.sumArrayUsingReduce(inputArray);

    assertThat(sum).isZero();
  }

  @Test
  public void givenInputArrayWithOneElement_whenSumArrayUsingReduce_thenSumReturned() {
    int[] inputArray = { 1 };

    int sum = Chapter3.sumArrayUsingReduce(inputArray);

    assertThat(sum).isEqualTo(1);
  }

  @Test
  public void givenInputArrayWithMultipleElements_whenSumArrayUsingReduce_thenSumReturned() {
    int[] inputArray = {1, -2, 3, 4, 5};

    int sum = Chapter3.sumArrayUsingReduce(inputArray);

    assertThat(sum).isEqualTo(11);
  }

  @Test
  public void givenEmptyStream_whenAddUp_thenReturnsZero() {
    Stream<Integer> inputStream = Stream.empty();

    int sum = Chapter3.addUp(inputStream);

    assertThat(sum).isZero();
  }

  @Test
  public void givenStreamsWithElements_whenAddUp_thenSumIsReturned() {
    Stream<Integer> inputStream = Stream.of(1, -3, 4, 5, 10);

    int sum = Chapter3.addUp(inputStream);

    assertThat(sum).isEqualTo(17);
  }
}
