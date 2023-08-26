package com.tech.virtualpower.manager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BackgroundTaskManager extends AbstractExecutorService {
    private static final int RUNNING = 0;
    private static final int SHUTDOWN = 1;
    private static final int TERMINATED = 2;

    final Lock lock = new ReentrantLock();
    final Condition termination = lock.newCondition();

    final Executor underlyingExecutor;
    volatile ArrayDeque<Runnable> backgroundTaskQueue;

    volatile int state = RUNNING;
    private Runnable currentCommand;

    /**
     * Creates a new {@link BackgroundTaskManager}. <p>
     *
     * @param underlyingExecutor
     *            The underlying executor to use for executing the tasks
     *            submitted into this executor.
     */
    public BackgroundTaskManager(Executor underlyingExecutor) {
        this.underlyingExecutor = underlyingExecutor;
        this.backgroundTaskQueue = new ArrayDeque<Runnable>();
    }


    /*
     * The runnable we submit into the underlyingExecutor, we avoid creating
     * unnecessary runnables since only one will be submitted at a time
     */

    private final Runnable innerRunnable = new Runnable() {

        public void run() {
            /*
             * If state is TERMINATED, skip execution
             */
            if (state == TERMINATED) {
                return;
            }

            try {
                currentCommand.run();
            } finally {
                lock.lock();
                try {
                    currentCommand = backgroundTaskQueue.pollFirst();
                    if (currentCommand != null && state < TERMINATED) {
                        try {
                            underlyingExecutor.execute(this);
                        } catch (Exception e) {
                            //The underlying executor may have been shutdown.
                            //We would need a kind of handler for this.
                            //Terminate this executor and clean pending command for now
                            currentCommand = null;
                            backgroundTaskQueue.clear();
                            transitionToTerminated();
                        }
                    } else {
                        if (state == SHUTDOWN) {
                            transitionToTerminated();
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    };

    public void addAppTaskInQueue(Runnable runnable) {
        lock.lock();
        try {
            if (state != RUNNING) {
                return;
            }
            if (currentCommand == null && backgroundTaskQueue.isEmpty()) {
                currentCommand = runnable;
                underlyingExecutor.execute(innerRunnable);
            } else {
                backgroundTaskQueue.add(runnable);
            }
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void execute(Runnable command) {

        lock.lock();
        try {
            if (state != RUNNING) {
                return;
            }
            if (currentCommand == null && backgroundTaskQueue.isEmpty()) {
                currentCommand = command;
                underlyingExecutor.execute(innerRunnable);
            } else {
                backgroundTaskQueue.add(command);
            }
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void shutdown() {

        lock.lock();
        try {
            if (state == RUNNING) {
                if (currentCommand == null && backgroundTaskQueue.isEmpty()) {
                    transitionToTerminated();
                } else {
                    state = SHUTDOWN;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {

        lock.lock();
        try {
            if (state < TERMINATED) {
                transitionToTerminated();
                ArrayList<Runnable> result = new ArrayList<Runnable>(backgroundTaskQueue);
                backgroundTaskQueue.clear();
                return result;
            }
            return Collections.<Runnable>emptyList();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isShutdown() {
        return state > RUNNING;
    }

    @Override
    public boolean isTerminated() {
        return state == TERMINATED;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            while (!isTerminated() && nanos > 0) {
                nanos = termination.awaitNanos(nanos);
            }
        } finally {
            lock.unlock();
        }
        return isTerminated();
    }

    private void transitionToTerminated() {
        state = TERMINATED;
        termination.signalAll();
    }


}
