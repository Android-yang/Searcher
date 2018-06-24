
package com.android.yangke.http;


interface TaskWatchable {

    void setWatcher(ITaskWatcher watcher);

    ITaskWatcher getWatcher();
}

interface ITaskRunner {

    void cancel();

    boolean isCancelled();

    ITaskRunner EMPTY_RUNNER = new ITaskRunner() {

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCancelled() {
            return true;
        }

    };
}

public interface ITask extends TaskWatchable {
    /**
     * @return true if success, otherwise return false
     */
    boolean doWork();

    void cancel();

    boolean isCancelled();

    void setRunner(ITaskRunner runner);

}
