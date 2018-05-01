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

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.codesod.example.executor.MonitoredThreadPoolExecutor.MonitoringKey.activeThreads;
import static com.codesod.example.executor.MonitoredThreadPoolExecutor.MonitoringKey.failedTasks;
import static com.codesod.example.executor.MonitoredThreadPoolExecutor.MonitoringKey.maxPoolSize;
import static com.codesod.example.executor.MonitoredThreadPoolExecutor.MonitoringKey.queueSize;
import static com.codesod.example.executor.MonitoredThreadPoolExecutor.MonitoringKey.successfulTasks;
import static com.codesod.example.executor.MonitoredThreadPoolExecutor.MonitoringKey.taskExecution;

/**
 * @author MD Sayem Ahmed
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
public class MonitoredThreadPoolExecutor extends ThreadPoolExecutor {
  private final MetricRegistry metricRegistry;
  private final String metricsPrefix;
  private ThreadLocal<Timer.Context> taskExecutionTimer = new ThreadLocal<>();

  public MonitoredThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      MetricRegistry metricRegistry,
      String poolName
  ) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    this.metricRegistry = metricRegistry;
    this.metricsPrefix = MetricRegistry.name(getClass(), poolName);
    registerGauges();
  }

  public MonitoredThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      MetricRegistry metricRegistry,
      String poolName
  ) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    this.metricRegistry = metricRegistry;
    this.metricsPrefix = MetricRegistry.name(getClass(), poolName);
    registerGauges();
  }

  public MonitoredThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      RejectedExecutionHandler handler,
      MetricRegistry metricRegistry,
      String poolName
  ) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    this.metricRegistry = metricRegistry;
    this.metricsPrefix = MetricRegistry.name(getClass(), poolName);
    registerGauges();
  }

  public MonitoredThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler,
      MetricRegistry metricRegistry,
      String poolName
  ) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    this.metricRegistry = metricRegistry;
    this.metricsPrefix = MetricRegistry.name(getClass(), poolName);
    registerGauges();
  }

  @Override
  protected void beforeExecute(Thread thread, Runnable task) {
    super.beforeExecute(thread, task);
    Timer timer = metricRegistry.timer(MetricRegistry.name(metricsPrefix, taskExecution));
    taskExecutionTimer.set(timer.time());
  }

  @Override
  protected void afterExecute(Runnable task, Throwable throwable) {
    Timer.Context context = taskExecutionTimer.get();
    context.stop();

    super.afterExecute(task, throwable);
    if (throwable == null && task instanceof Future<?> && ((Future<?>) task).isDone()) {
      try {
        ((Future<?>) task).get();
      } catch (CancellationException ce) {
        throwable = ce;
      } catch (ExecutionException ee) {
        throwable = ee.getCause();
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
    }
    if (throwable != null) {
      Counter failedTasksCounter = metricRegistry.counter(MetricRegistry.name(metricsPrefix, failedTasks));
      failedTasksCounter.inc();
    } else {
      Counter successfulTasksCounter = metricRegistry.counter(
          MetricRegistry.name(metricsPrefix, successfulTasks));
      successfulTasksCounter.inc();
    }
  }

  private void registerGauges() {
    metricRegistry.register(MetricRegistry.name(metricsPrefix, MonitoringKey.corePoolSize),
        (Gauge<Integer>) this::getCorePoolSize);
    metricRegistry.register(MetricRegistry.name(metricsPrefix, activeThreads),
        (Gauge<Integer>) this::getActiveCount);
    metricRegistry.register(MetricRegistry.name(metricsPrefix, maxPoolSize),
        (Gauge<Integer>) this::getMaximumPoolSize);
    metricRegistry.register(MetricRegistry.name(metricsPrefix, queueSize),
        (Gauge<Integer>) () -> getQueue().size());
  }

  static class MonitoringKey {
    static final String taskExecution = "task-execution";
    static final String failedTasks = "failed-tasks";
    static final String successfulTasks = "successful-tasks";
    static final String corePoolSize = "corePoolSize";
    static final String activeThreads = "activeThreads";
    static final String maxPoolSize = "maxPoolSize";
    static final String queueSize = "queueSize";
  }
}
