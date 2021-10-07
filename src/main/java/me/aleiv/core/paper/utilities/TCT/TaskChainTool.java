package me.aleiv.core.paper.utilities.TCT;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Quick util that allows developer to chain tasks together without having to
 * think much about it
 */
public class TaskChainTool {
    /**
     * Use concurrent linked queue to preserve order and ensure many threads can
     * access the same resources at once.
     */
    private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Runnable poll() {
        return queue.poll();
    }

    /**
     * Builder methods that appends runnable to the end of the queue.
     * 
     * @param runnable to append to the queue.
     * @return this instance of TaskChainTool.
     */
    public TaskChainTool add(Runnable runnable) {
        queue.add(runnable);

        return this;
    }

    /**
     * Builder method that appends delay to the end of the queue.
     * 
     * @param delay in milliseconds.
     * @return this instance of TaskChainTool instance.
     */
    public TaskChainTool delay(long delay) {
        final var actualDelay = Math.abs(delay);

        /** Ensure the delay is greater than 0ms */
        if (actualDelay > 0)
            queue.add(DelayTask.of(actualDelay));

        return this;
    }

    /**
     * Builder method to add a task to the chain.
     * 
     * @param runnable The task to add.
     * @param delay    The delay in milliseconds after the task is executed.
     * @return The builder object.
     */
    public TaskChainTool addWithDelay(Runnable runnable, long delay) {
        queue.add(runnable);

        final var actualDelay = Math.abs(delay);

        /** Ensure the delay is greater than 0ms */
        if (actualDelay > 0)
            queue.add(DelayTask.of(actualDelay));

        return this;
    }

    /**
     * Function that executes all tasks in the queue.
     * 
     * @return CompletableFuture that completes when all tasks are completed.
     */
    public CompletableFuture<Boolean> execute() {
        return CompletableFuture.supplyAsync(() -> {
            /** Loop to execute all tasks in order. */
            while (!queue.isEmpty())
                handleRunnable(poll());

            return true;
        });
    }

    /**
     * Function that executes all tasks in the queue.
     * 
     * @param nextRunnable
     */
    public void handleRunnable(Runnable nextRunnable) {
        var thread = new Thread(nextRunnable);
        try {
            /**
             * Join the thread so that it waits until whatever needs to be executed gets
             * executed.
             */
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}