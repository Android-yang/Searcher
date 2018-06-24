
package com.android.yangke.http;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

public class TaskMaker {

    static NetworkTask buildNetworkTask(BaseParam param, ServiceMap serviceMap, Bitmap map,
                                        File dir, String message, Serializable ext, RequestListener listener, int[] features) {
        NetworkTask task = null;
        if (HttpPar.HTTP_POST.equals(param.getRequestMethod())) {
            task = new HttpPostTask(param, serviceMap, message, ext, features);
        } else {
            task = new HttpGetTask(param, serviceMap, message, ext, features);
        }
        NetworkTaskWatcherAdapter watcher = new NetworkTaskWatcherAdapter(listener);
        task.setWatcher(watcher);
        return task;
    }
}
