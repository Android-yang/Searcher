
package com.android.yangke.http;

public interface RequestListenerSwitcher {

    void openListener();

    void closeListener();

    boolean isListenerOn();
}
