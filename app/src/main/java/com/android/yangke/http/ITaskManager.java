package com.android.yangke.http;


public interface ITaskManager {
    void addTask(ITask task);

    void removeTaskAndRunNext(ITask task);

    void cancelTasks(TaskMatcher matcher);
}