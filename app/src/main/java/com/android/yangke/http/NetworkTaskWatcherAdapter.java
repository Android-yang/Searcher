package com.android.yangke.http;

class NetworkTaskWatcherAdapter implements ITaskWatcher {
    private final RequestListener mListener;

    NetworkTaskWatcherAdapter(RequestListener handler) {
        this.mListener = handler;
    }

    @Override
    public void onTaskCancel(ITask task) {
    }

    @Override
    public void onTaskStart(ITask task) {
        getAvailableListener().onRequestStart((NetworkTask)task);
    }

    @Override
    public void onTaskSuccess(ITask task) {
        NetworkTask netTask = (NetworkTask) task;
        if (ResponseCode.CODE_200 == netTask.response.mStatus) {
            getAvailableListener().onRequestSuccess(netTask);
        } else {
            getAvailableListener().onRequestFail(netTask);
            getAvailableListener().onFail(netTask);
        }
    }

    @Override
    public void onTaskError(ITask task) {
        NetworkTask netTask = ((NetworkTask)task);
        netTask.response = NetErrorResponse.INSTANCE;
        getAvailableListener().onNetError(netTask);
        getAvailableListener().onFail(netTask);
    }

    @Override
    public void onTaskEnd(ITask task) {
        getAvailableListener().onRequestEnd((NetworkTask)task);
    }

    private RequestListener getAvailableListener() {
        return mListener.isListenerOn() ? mListener : RequestListener.EMPTY_LISTENER;
    }

    public RequestListener getListener() {
        return mListener;
    }

    @Override
    public int hashCode() {
        int result = 21;
        return result = 31 * result + mListener.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj != null && obj.getClass() == getClass()) {
            NetworkTaskWatcherAdapter watcher = (NetworkTaskWatcherAdapter) obj;
            if (watcher.getListener() == getListener()) {
                return true;
            }
        }
        return false;
    }
}