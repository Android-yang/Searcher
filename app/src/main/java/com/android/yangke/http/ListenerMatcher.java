
package com.android.yangke.http;

public class ListenerMatcher implements TaskMatcher {

    private RequestListener mListener;

    public ListenerMatcher(RequestListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean isMatch(ITask task) {
        ITaskWatcher watcher = task.getWatcher();
        if (watcher instanceof NetworkTaskWatcherAdapter) {
            NetworkTaskWatcherAdapter listenerWatcher = (NetworkTaskWatcherAdapter) watcher;
            if (listenerWatcher.getListener() == mListener) {
                return true;
            }
        }
        return false;
    }

}
