package com.android.yangke.http;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.android.yangke.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 挡板类，保存了保存响应报文到文件中，文件名为请求报文的接口方法名。
 */
public class MessageWarehouse {
    private static final String TAG = "MessageWarehouse";

    private static final String WAREHOUSE_DIR = "dazong_warehouse";

    /**
     * Write response message to warehouse.
     */
    public static boolean writeResponseContent(String userMobile, String interfaceName, InputStream inputStream) {
        File responseFile = getResponseFile(userMobile, interfaceName);
        if (responseFile == null) {
            return false;
        }
        if (responseFile != null && responseFile.exists()) {
            logd("replace existing response message file.");
        }

        File parent = responseFile.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        return saveToFile(inputStream, responseFile);
    }

    private static boolean saveToFile(InputStream inputStream, File file) {
        boolean result = false;
        final int IO_BUFFER_SIZE = 8 * 1024;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(inputStream, IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(file), IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            out.flush();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static File getResponseFile(String userMobile, String interfaceName) {
        File userDir = getUserDir(userMobile);
        return new File(userDir, interfaceName);
    }

    /**
     * Get response content from warehouse which stored in external storage.
     */
    public static InputStream getResponseContentFromExternalStorage(String userMobile, String interfaceName)
            throws IOException {
        Log.d(TAG, "userMobile: " + userMobile + ", interface url: " + interfaceName);
        File responseFile = getResponseFile(userMobile, interfaceName);
        if (responseFile == null || !responseFile.exists()) {
            throw new IOException("Can't find corresponding response message: "
                    + "userMobile: " + userMobile + ", method: " + interfaceName);
        }

        FileInputStream inputStream = new FileInputStream(responseFile);
        return inputStream;
    }

    public static InputStream getResponseContent(Context context, String userMobile, String interfaceName) throws IOException {
        String responseFilePath = "";
        if (TextUtils.isEmpty(userMobile)) {
            responseFilePath = interfaceName;
        } else {
            responseFilePath = userMobile + File.pathSeparator + interfaceName;
        }
        InputStream inStream = null;
        try {
            inStream = getResponseContentFromExternalStorage(userMobile, interfaceName);
        } catch (IOException e) {
            Log.d(TAG, "Can't find corresponding response message from external storage.");
        }
        if (inStream == null) {
            inStream = context.getAssets().open(responseFilePath);
        }
        return inStream;
    }

    private static void logd(String log) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, log);
        }
    }

    private static void loge(String log) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, log);
        }
    }

    private static File getMessageWarehouseDir() {
        if (!isExternalStorageAvailable()) {
            loge("External storage unavailable.");
            return null;
        }
        return CacheUtils.getCacheFile(WAREHOUSE_DIR);
    }

    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static File getUserDir(String userMobile) {
        File warehouse = getMessageWarehouseDir();
        if (TextUtils.isEmpty(userMobile)) {
            return warehouse;
        } else {
            return new File(warehouse, userMobile);
        }
    }
}
