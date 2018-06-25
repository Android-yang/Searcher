package com.android.yangke.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;

import com.android.yangke.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.vondear.rxtools.view.RxToast;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class ApkDownloadService extends Service {

    public static final String KEY_APK_FILE_PATH = "apk_file_path";
    public static final String KEY_APK_URL = "apk_url";
    public static final String KEY_APK_NAME = "apk_name";
    public static final String SYSTEM_DEFAULT_INSTALL_APK_PARAM = "application/vnd.android.package-archive";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String apkUrl = intent.getStringExtra(KEY_APK_URL);
        String apkName = intent.getStringExtra(KEY_APK_NAME);
        String apkPath = intent.getStringExtra(KEY_APK_FILE_PATH);
        OkGo.get(apkUrl)//
                .tag(this)//
                .execute(new FileCallback(apkPath, apkName) {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, okhttp3.Response response) {
                        if (ActivityCompat.checkSelfPermission(ApkDownloadService.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            RxToast.warning("应用未获得 SD 卡权限!");
                            return;
                        }
                        File apkFile = new File(file.getAbsolutePath());
                        installApk(apkFile);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        RxToast.warning("APK 下载失败");
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                        progress = Math.round(progress * 100);
                        downloadApkNotification(ApkDownloadService.this, progress);
                    }
                });
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 安装 apk 文件
     *
     * @param apkFile
     */
    public void installApk(File apkFile) {
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            String authority = getPackageName() + ".fileprovider";
            Uri apkUriFile = FileProvider.getUriForFile(getApplicationContext(), authority, apkFile);
            installApkIntent.setDataAndType(apkUriFile, SYSTEM_DEFAULT_INSTALL_APK_PARAM);
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), SYSTEM_DEFAULT_INSTALL_APK_PARAM);
        }
        if (getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            startActivity(installApkIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void downloadApkNotification(Context context, float progress) {
        NotificationManager downloadApkNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int notificationId = 0;
        String channelID = "1";
        String channelName = "channel_name";
        String download_hint = "已下载：" + (int) progress  + "%";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName,
                    NotificationManager.IMPORTANCE_LOW);
            Notification.Builder builder = new Notification.Builder(context, channelID);
            Notification notification = builder
                    .setSmallIcon(android.R.drawable.stat_sys_download)//smallIcon 必备
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(getString(R.string.app_name))
                    .setProgress(100, (int) progress, false)
                    .setContentText(download_hint)
                    .build();
            downloadApkNotificationManager.createNotificationChannel(channel);
            downloadApkNotificationManager.notify(notificationId, notification);
        } else {
            Notification notification = new Notification();
            notification.icon = android.R.drawable.stat_sys_download;
            // 放置在"正在运行"栏目中
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notification.tickerText = getString(R.string.app_name);
            notification.when = System.currentTimeMillis();
            notification.defaults = Notification.DEFAULT_LIGHTS;
            // 设置任务栏中下载进程显示的views
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.notifycation_apk_downloadxml);

            notification.contentView = views;
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, ApkDownloadService.class), PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentIntent = contentIntent;
            views.setTextViewText(R.id.notification_txt_progress, download_hint);
            views.setProgressBar(R.id.notification_download_apk_progress_Bar, 100,
                    (int) progress, false);
            downloadApkNotificationManager.notify(notificationId, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}