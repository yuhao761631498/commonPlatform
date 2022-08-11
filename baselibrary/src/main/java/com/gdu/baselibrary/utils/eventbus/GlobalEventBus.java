package com.gdu.baselibrary.utils.eventbus;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wixche
 */
public class GlobalEventBus {
    private static EventBus sBus;
    private static Lock sLock = new ReentrantLock();

    private GlobalEventBus() {
    }

    public static EventBus getBus() {
        if (sBus == null) {
            sLock.lock();
            try {
                if (sBus == null) {
                    sBus = EventBus.getDefault();
                }
            } finally {
                sLock.unlock();
            }
        }
        return sBus;
    }

}
