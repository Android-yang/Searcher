
package com.android.yangke.http;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

public final class Request {

    private Request() { }

    public static void startRequest(BaseParam param, ServiceMap serviceMap,
                                    RequestListener watcher, int... features) {
        startRequest(param, null, serviceMap, watcher, null, null, null, features);
    }

    public static void startRequest(BaseParam param, ServiceMap serviceMap,
                                    RequestListener watcher, String message, int... features) {
        startRequest(param, null, serviceMap, watcher, null, null, message, features);
    }

    public static void startRequest(BaseParam param, Serializable ext,
                                    ServiceMap serviceMap, RequestListener watcher, int... features) {
        startRequest(param, ext, serviceMap, watcher, null, null, null, features);
    }

    /**
     * 发业务接口请求
     *
     * @param param
     * @param ext        不需要传到服务器的参数，用于返回时使用，常用的值为是否加载更多
     * @param serviceMap
     * @param message    进度框message
     * @param features   请求的附加参数
     */
    public static void startRequest(BaseParam param, Serializable ext,
                                    ServiceMap serviceMap, RequestListener watcher,
                                    String message, int... features) {
        startRequest(param, ext, serviceMap, watcher, null, null, message, features);
    }

    private static void startRequest(BaseParam param, Serializable ext,
                                     ServiceMap serviceMap, RequestListener watcher, Bitmap map, File dir,
                                     String message, int... features) {
        final NetworkTask task = TaskMaker.buildNetworkTask(param, serviceMap, map, dir, message, ext, watcher, features);
        TaskManager.getInstance().addTask(task);
    }

    /**
     * 发业务接口请求
     *
     * @param param
     * @param serviceMap
     * @param message    进度框message
     * @param map        上传图片
     * @param features   请求的附加参数
     */
    public static void startUpload(BaseParam param, ServiceMap serviceMap,
                                   RequestListener watcher, String message, Bitmap map,
                                   File dir, int... features) {
        startRequest(param, null, serviceMap, watcher, map, dir, message, features);
    }
}
