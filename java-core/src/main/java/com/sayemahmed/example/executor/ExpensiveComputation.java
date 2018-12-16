package com.sayemahmed.example.executor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ExpensiveComputation {
  private final int id;

  ExpensiveComputation(int id) {
    this.id = id;
  }

  void compute() {
    try {
      log.info("{} - I am trying to compute some heavy stuff. Current thread interruption status: {}.",
          id, Thread.currentThread().isInterrupted());
      Thread.sleep(1000);
      log.info("{} - I finished my computation", id);
    } catch (InterruptedException e) {
      log.error("{} - I was interrupted during my computation.", id, e);
      Thread.currentThread().interrupt();
    }
  }
}
