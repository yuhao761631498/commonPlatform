package com.gdu.baselibrary.utils.schedule;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryBuilder {
    private String nameFormat = null;
    private Boolean daemon = null;
    private Integer priority = null;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
    private ThreadFactory backingThreadFactory = null;

    public ThreadFactoryBuilder() {
    }

    public ThreadFactoryBuilder setNameFormat(String nameFormat) {
        String.format(nameFormat, 0);
        this.nameFormat = nameFormat;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        Preconditions.checkArgument(priority >= 1, "Thread priority (%s) must be >= %s", new Object[]{priority, 1});
        Preconditions.checkArgument(priority <= 10, "Thread priority (%s) must be <= %s", new Object[]{priority, 10});
        this.priority = priority;
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = Preconditions.checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = Preconditions.checkNotNull(backingThreadFactory);
        return this;
    }

    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        final String nameFormat = builder.nameFormat;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        final ThreadFactory backingThreadFactory = builder.backingThreadFactory;
        final AtomicLong count = nameFormat != null ? new AtomicLong(0L) : null;
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = backingThreadFactory != null ? backingThreadFactory.newThread(runnable) : new Thread(runnable);
                if (nameFormat != null) {
                    thread.setName(String.format(nameFormat, count.getAndIncrement()));
                }

                if (daemon != null) {
                    thread.setDaemon(daemon);
                }

                if (priority != null) {
                    thread.setPriority(priority);
                }

                if (uncaughtExceptionHandler != null) {
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }

                return thread;
            }
        };
    }
}