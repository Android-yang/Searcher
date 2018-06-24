
package com.android.yangke.http;

import android.net.Uri;
import android.os.AsyncTask;

import java.util.Iterator;
import java.util.LinkedList;

public class TaskManager implements ITaskManager {

    private static class TaskRunner extends AsyncTask<Void, Integer, Boolean> implements ITaskRunner {

        private final ITask task;
        private final ITaskManager iTaskManager;

        public TaskRunner(ITask netTask, ITaskManager taskManager) {
            task = netTask;
            iTaskManager = taskManager;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notifyOnTaskStart();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            notifyOnTaskCancel();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = false;
            result = task.doWork();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                notifyOnTaskSuccess();
            } else {
                notifyOnTaskError();
            }
            notifyOnTaskEnd();
        }

        private void notifyOnTaskEnd() {
            getWatcher().onTaskEnd(task);
            iTaskManager.removeTaskAndRunNext(task);
        }

        private void notifyOnTaskSuccess() {
            getWatcher().onTaskSuccess(task);
        }

        private void notifyOnTaskError() {
            getWatcher().onTaskError(task);
        }

        private void notifyOnTaskCancel() {
            getWatcher().onTaskCancel(task);
            getWatcher().onTaskEnd(task);
            iTaskManager.removeTaskAndRunNext(task);
        }

        private void notifyOnTaskStart() {
            getWatcher().onTaskStart(task);
        }

        private ITaskWatcher getWatcher() {
            return task.getWatcher();
        }

        private boolean isRunning() {
            return getStatus() == Status.RUNNING;
        }

        @Override
        public void cancel() {
            task.setWatcher(ITaskWatcher.EMPTY_WATCHER);
            cancel(true);
        }
    }

    private static TaskManager singleInstance = null;

    public static ITaskManager getInstance() {
        synchronized (TaskManager.class) {
            if (singleInstance == null) {
                singleInstance = new TaskManager();
            }
        }
        return singleInstance;
    }

    private final LinkedList<TaskRunner> listSequence = new LinkedList<TaskRunner>();

    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    // 电信CTWAP时apn的名称:#777,ctwap
    public static final String CTWAP_APN_NAME_1 = "#777";
    public static final String CTWAP_APN_NAME_2 = "ctwap";

    private TaskManager() {
    }
//
    @Override
    public void addTask(ITask newTask) {
        addTask(newTask, TaskFeature.ADD_ONORDER);
    }

    private void addTask(ITask newTask, TaskFeature addType) {
        TaskRunner newQTask = new TaskRunner(newTask, this);
        synchronized (this.listSequence) {
            Iterator<TaskRunner> listSequenceIterator = this.listSequence.iterator();
            while (listSequenceIterator.hasNext()) {
                TaskRunner task = listSequenceIterator.next();
                TaskRunner tmp = task;
                if (tmp.task.equals(newTask)) {
                    return;
                }
            }

            switch (addType) {
                case ADD_ONORDER:
                    this.listSequence.add(newQTask);
                    break;
                case ADD_INSERT2HEAD:
                    this.listSequence.add(0, newQTask);
                    break;
                case ADD_CANCELPRE: {
                    cancelAllTask();
                    this.listSequence.add(0, newQTask);
                }
                    break;
                default:
                    listSequence.add(newQTask);
                    break;
            }
        }
        checkTasks();
    }

    @Override
    public void cancelTasks(TaskMatcher matcher) {
        synchronized (this.listSequence) {
            Iterator<TaskRunner> it = this.listSequence.iterator();
            while (it.hasNext()) {
                TaskRunner task = it.next();
                if (matcher.isMatch(task.task)) {
                    task.cancel();
                    it.remove();
                }
            }
        }
    }

    private void cancelAllTask() {
        synchronized (this.listSequence) {
            Iterator<TaskRunner> it = this.listSequence.iterator();
            while (it.hasNext()) {
                TaskRunner task = it.next();
                task.cancel();
                it.remove();
            }
        }
    }

    private void checkTasks() {
        if (this.listSequence.size() == 0) {
            return;
        }
        synchronized (this.listSequence) {
            Iterator<TaskRunner> it = this.listSequence.iterator();
            while (it.hasNext()) {
                TaskRunner nt = it.next();
                if (nt.isRunning()) continue;
                nt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    public void destroy() {
        if (singleInstance != null) {
            singleInstance.cancelAllTask();
        }
        singleInstance = null;
    }

    @Override
    public void removeTaskAndRunNext(ITask taskFinishedOrCancelled) {
        synchronized(listSequence) {
            Iterator<TaskRunner> iterator = listSequence.iterator();
            while(iterator.hasNext()) {
                TaskRunner t = iterator.next();
                if (t.task == taskFinishedOrCancelled) {
                    t.cancel(true);
                    iterator.remove();
                }
            }
        }
        checkTasks();
    }

}
