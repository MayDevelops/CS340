package edu.byu.cs.tweeter.client.backgroundTask.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TaskExecutor<T> {

  public TaskExecutor(T task) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute((Runnable) task);
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public TaskExecutor(T taskOne, T taskTwo) {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    executor.execute((Runnable) taskOne);
    executor.execute((Runnable) taskTwo);
  }

}
