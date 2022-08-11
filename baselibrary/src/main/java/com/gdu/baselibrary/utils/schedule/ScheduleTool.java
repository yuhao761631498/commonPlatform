package com.gdu.baselibrary.utils.schedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScheduleTool {
    private static ScheduledExecutorService mExecutor;
    private static ExecutorService mThreadPoolExecutor;
    private static Lock mLock = new ReentrantLock();
    private static ScheduleTool instance;

    private ScheduleTool() {
    }

    public static ScheduleTool getInstance() {
        if (instance == null) {
            mLock.lock();
            try {
                if (instance == null) {
                    instance = new ScheduleTool();
                }
            } finally {
                mLock.unlock();
            }
        }
        return instance;
    }

    public ScheduledExecutorService getTimerSchedule() {
        // 多线程并行处理定时任务时，Timer运行多个TimeTask
        // 时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。
        //org.apache.commons.lang3.concurrent.BasicThreadFactory
        if (mExecutor == null) {
            mExecutor = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("timer-schedule-pool-1").daemon(true).build());
        }
        return mExecutor;
    }

    public ExecutorService getThread() {
        // 线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。
        // 说明：使用线程池的好处是减少在创建和销毁线程上所花的时间以及系统资源的开销，解决资源不足的问题。
        // 如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。
        if (mThreadPoolExecutor == null) {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("thread-pool-1").build();
            mThreadPoolExecutor = new ThreadPoolExecutor(1, 3,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory,
                    new ThreadPoolExecutor.AbortPolicy());
        }
        return mThreadPoolExecutor;
    }

}
