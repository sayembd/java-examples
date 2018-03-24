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

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author MD Sayem Ahmed
 */
class Chapter3 {

  static int sumArrayUsingReduce(int[] numbers) {
    return Arrays.stream(Objects.requireNonNull(numbers, "Input array cannot be null"))
        .reduce(0, (accumulator, element) -> accumulator + element);
  }

  static int addUp(Stream<Integer> numbers) {
    return Objects.requireNonNull(numbers, "Input cannnot be null")
        .reduce(0, (accumulator, element) -> accumulator + element);
  }
}
