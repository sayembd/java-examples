package com.codesod.example.core.concurrency;

import java.util.concurrent.Callable;

public class VoidCallableRunner {

  public static void main(String[] args) throws Exception {
    Callable<Void> voidCallable = () -> {
      System.out.println("I am void callable");
      return null;
    };

    voidCallable.call();
  }
}
