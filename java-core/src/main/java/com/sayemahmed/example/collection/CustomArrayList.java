package com.sayemahmed.example.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class CustomArrayList<E> implements List<E> {

  private Object[] elements;
  private int nextIndex;

  public CustomArrayList() {
    elements = new Object[1];
    nextIndex = 0;
  }

  @Override
  public boolean add(E element) {
    if (nextIndex == elements.length) {
      Object[] copy = new Object[nextIndex * 2];
      System.arraycopy(elements, 0, copy, 0, nextIndex);
      elements = copy;
    }
    elements[nextIndex] = element;
    nextIndex++;
    return true;
  }

  @Override
  public int size() {
    return nextIndex;
  }

  @SuppressWarnings("unchecked")
  @Override
  public E remove(int index) {
    /*
     * Specification: if the index is out of bound then throw
     * IndexOutOfBoundsException.
     */
    if (index < 0 || index >= nextIndex) {
      throw new IndexOutOfBoundsException(
          "index " + index + " is out of range"
      );
    }
    E elementBeingRemoved = (E) elements[index];
    nextIndex--;
    System.arraycopy(elements, index + 1, elements, index, nextIndex - index);
    elements[nextIndex] = null;
    return elementBeingRemoved;
  }

  @Override
  public void add(int index, E element) {
    if (index < 0 || index > size()) {
      throw new IndexOutOfBoundsException("index must be between 0 and " + size());
    }
    if (index == size()) {
      add(element);
      return;
    }
    if (nextIndex == elements.length) {
      Object[] copy = new Object[nextIndex * 2];
      System.arraycopy(elements, 0, copy, 0, index);
      copy[index] = element;
      System.arraycopy(elements, index, copy, index + 1, nextIndex - index);
      elements = copy;
    } else {
      System.arraycopy(elements, index, elements, index + 1, nextIndex - index);
      elements[index] = element;
    }
    nextIndex++;
  }

  @Override
  public boolean contains(Object element) {
    return Arrays.stream(elements)
        .anyMatch(existingElement -> Objects.equals(existingElement, element));
  }

  @Override
  public boolean remove(Object element) {
    for (int i = 0; i < nextIndex; i++) {
      if (Objects.equals(elements[i], element)) {
        remove(i);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public Iterator<E> iterator() {
    return new CustomArrayListIterator();
  }

  @Override
  public Object[] toArray() {
    return new Object[0];
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return null;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  @Override
  public void clear() {

  }

  @Override
  public E get(int index) {
    return null;
  }

  @Override
  public E set(int index, E element) {
    return null;
  }

  @Override
  public int indexOf(Object o) {
    return 0;
  }

  @Override
  public int lastIndexOf(Object o) {
    return 0;
  }

  @Override
  public ListIterator<E> listIterator() {
    return null;
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return null;
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return null;
  }

  private class CustomArrayListIterator implements Iterator<E> {

    private final int size = CustomArrayList.this.size();
    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E next() {
      return (E) elements[index++];
    }
  }
}
