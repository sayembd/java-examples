package com.sayemahmed.example.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class CustomArrayListTest {

  @Test
  void testAdd() {
    var list = new CustomArrayList<Integer>();
    IntStream.rangeClosed(1, 256)
        .forEach(list::add);

    assertThat(list)
        .containsExactlyElementsOf(IntStream.rangeClosed(1, 256).boxed().toList());
  }

  @Test
  void testSize() {
    var list = new CustomArrayList<Integer>();
    assertThat(list.size()).isZero();
    list.add(1);
    assertThat(list).hasSize(1);
    list.add(2);
    assertThat(list).hasSize(2);
  }

  @Test
  void testRemoveWithIndex() {
    var list = new CustomArrayList<Integer>();
    list.add(1);
    list.remove(0);
    assertThat(list.size()).isZero();
    list.add(1);
    list.add(2);
    list.remove(1);
    assertThat(list).containsExactly(1);
    list.remove(0);
    assertThat(list.size()).isZero();
    list.add(6);
    list.add(7);
    list.add(8);
    list.remove(2);
    assertThat(list).containsExactly(6, 7);
    list = new CustomArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    list.remove(2);
    list.remove(1);
    list.remove(0);
    assertThat(list).isEmpty();
  }

  @Test
  void testAddAtIndex() {
    var list = new CustomArrayList<Integer>();
    list.add(0, 1);
    assertThat(list)
        .containsExactly(1);
    assertThat(list.size())
        .isOne();
    list.add(0, 2);
    assertThat(list)
        .containsExactly(2, 1);
    assertThat(list.size())
        .isEqualTo(2);
    list.add(1, 3);
    assertThat(list)
        .containsExactly(2, 3, 1);
    assertThat(list.size())
        .isEqualTo(3);
    list.add(3, 4);
    assertThat(list)
        .containsExactly(2, 3, 1, 4);
    assertThat(list.size())
        .isEqualTo(4);
    list.add(2, 5);
    assertThat(list)
        .containsExactly(2, 3, 5, 1, 4);
    assertThat(list.size())
        .isEqualTo(5);
    list.add(3, 9);
    assertThat(list.size())
        .isEqualTo(6);
    list.add(4, 10);
    assertThat(list.size())
        .isEqualTo(7);
    list.add(2, 16);
    assertThat(list.size())
        .isEqualTo(8);
    list.add(6, 15);
    assertThat(list.size())
        .isEqualTo(9);
    assertThat(list)
        .containsExactly(2, 3, 16, 5, 9, 10, 15, 1, 4);
  }

  @Test
  void testContainsAndRemoveByIndex() {
    var list = new CustomArrayList<Integer>();
    assertThat(list.contains(1)).isFalse();
    list.add(1);
    assertThat(list.contains(1)).isTrue();
    list.remove(0);
    assertThat(list.contains(1)).isFalse();
    assertThat(list.contains(2)).isFalse();
    list.add(2);
    assertThat(list.contains(2)).isTrue();
    list.remove(0);
    assertThat(list.contains(2)).isFalse();
    list.add(1);
    list.add(2);
    assertThat(list.contains(2)).isTrue();
    list.remove(1);
    assertThat(list.contains(2)).isFalse();
    list.remove(0);
    assertThat(list.size()).isZero();
    list = new CustomArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    list.remove(1);
    assertThat(list)
        .containsExactly(1, 3);
  }
}