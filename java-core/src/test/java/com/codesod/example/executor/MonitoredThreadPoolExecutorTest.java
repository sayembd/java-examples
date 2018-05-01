/*
 * Copyright 2018 MD Sayem Ahmed
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
package com.codesod.example.executor;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author MD Sayem Ahmed
 */
@Slf4j
public class MonitoredThreadPoolExecutorTest {

  @Test
  public void runs_a_couple_of_executors() throws InterruptedException {
    MetricRegistry metricRegistry = metricRegistry();
    MonitoredThreadPoolExecutor firstExecutor = new MonitoredThreadPoolExecutor(1, 2, 1, MINUTES,
        new LinkedBlockingQueue<>(20), metricRegistry, "first-executor");
    MonitoredThreadPoolExecutor secondExecutor = new MonitoredThreadPoolExecutor(1, 2, 1, MINUTES,
        new LinkedBlockingQueue<>(20), metricRegistry, "second-executor");

    IntStream.rangeClosed(1, 20)
        .mapToObj(ExpensiveComputation::new)
        .forEach(task -> {
          firstExecutor.submit(task::compute);
          secondExecutor.submit(task::compute);
        });

    firstExecutor.shutdown();
    firstExecutor.awaitTermination(1, MINUTES);
    firstExecutor.shutdownNow();

    secondExecutor.shutdown();
    secondExecutor.awaitTermination(1, MINUTES);
    secondExecutor.shutdownNow();
  }

  @Test
  public void measures_task_execution_time() throws InterruptedException {
    MetricRegistry metricRegistry = metricRegistry();
    MonitoredThreadPoolExecutor executor = new MonitoredThreadPoolExecutor(1, 2, 1, MINUTES,
        new LinkedBlockingQueue<>(20), metricRegistry, "simple-executor");

    IntStream.rangeClosed(1, 20)
        .mapToObj(ExpensiveComputation::new)
        .forEach(task -> executor.submit(task::compute));

    executor.shutdown();
    executor.awaitTermination(1, MINUTES);
    executor.shutdownNow();
  }

  @Test
  public void counts_successful_and_failed_tasks() throws InterruptedException {
    MetricRegistry metricRegistry = metricRegistry();
    MonitoredThreadPoolExecutor simpleExecutor = new MonitoredThreadPoolExecutor(1, 2, 1, MINUTES,
        new LinkedBlockingQueue<>(20), metricRegistry, "simple-executor");

    for (int i = 0; i < 20; i++) {
      if (i % 2 == 0) {
        simpleExecutor.submit(() -> { throw new RuntimeException("Thrown error"); });
      } else {
        simpleExecutor.submit(() -> new ExpensiveComputation(1).compute());
      }
    }

    simpleExecutor.shutdown();
    simpleExecutor.awaitTermination(1, MINUTES);
    simpleExecutor.shutdownNow();
  }

  private MetricRegistry metricRegistry() {
    MetricRegistry metricRegistry = new MetricRegistry();
    ConsoleReporter.forRegistry(metricRegistry)
        .convertDurationsTo(SECONDS)
        .convertRatesTo(SECONDS)
        .build()
        .start(1, SECONDS);
    return metricRegistry;
  }
}
