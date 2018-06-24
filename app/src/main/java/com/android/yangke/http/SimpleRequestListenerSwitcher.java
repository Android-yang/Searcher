
package com.android.yangke.http;

public class SimpleRequestListenerSwitcher implements RequestListenerSwitcher {

    private final Object block = new Object();

    private volatile boolean available = true;

    @Override
    public void openListener() {
        synchronized (block) {
            available = true;
        }
    }

    @Override
    public void closeListener() {
        synchronized (block) {
            available = false;
        }
    }

    @Override
    public boolean isListenerOn() {
        synchronized (block) {
            return available;
        }
    }

}
