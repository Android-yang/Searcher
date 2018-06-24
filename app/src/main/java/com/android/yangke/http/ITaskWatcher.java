
package com.android.yangke.http;

import com.orhanobut.logger.Logger;

public interface ITaskWatcher {

    void onTaskCancel(ITask task);

    void onTaskStart(ITask task);

    void onTaskSuccess(ITask task);

    void onTaskError(ITask task);

    void onTaskEnd(ITask task);

    public static final ITaskWatcher EMPTY_WATCHER = new ITaskWatcher() {
        @Override
        public void onTaskCancel(ITask task) {
            Logger.d("empty onTaskCancel");
        }

        @Override
        public void onTaskStart(ITask task) {
            Logger.d("empty onTaskStart");
        }

        @Override
        public void onTaskSuccess(ITask task) {
            Logger.d("empty onTaskSuccess");
        }

        @Override
        public void onTaskError(ITask task) {
            Logger.d("empty onTaskError");
        }

        @Override
        public void onTaskEnd(ITask task) {
            Logger.d("empty onTaskEnd");
        }

    };
}
