package com.android.yangke.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.android.yangke.vo.DaoMaster;
import com.android.yangke.vo.DaoSession;
import com.vondear.rxtools.RxTool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ListIterator;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : BaseApplication
 */
public class BaseApplication extends Application {

    public static BaseApplication instance;
    public static Handler mMainHandler = new Handler(Looper.getMainLooper());

    private final LinkedHashMap<Class<? extends BaseActivity>, WeakReference<Context>> contextObjects =
            new LinkedHashMap<Class<? extends BaseActivity>, WeakReference<Context>>();
    private DaoMaster mDaoMaster;
    private DaoSession mSession;


    public static BaseApplication instance() {
        return instance;
    }

    public static void runUiThread(Runnable r) {
        mMainHandler.post(r);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 工具类初始化
        RxTool.init(this);

        iniDaoMaster();
    }

    /**
     * GreenDAO 使用前的初始化
     */
    private void iniDaoMaster() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(instance(), "search.db", null);
        mDaoMaster = new DaoMaster(helper.getWritableDb());
        mSession = mDaoMaster.newSession();
    }

    /**
     * @return 操作数据库的对象
     */
    public DaoSession getDaoSession() {
        return mSession;
    }

    public synchronized Context getActiveContext(Class<? extends BaseActivity> className) {
        WeakReference<Context> ref = contextObjects.get(className);
        if (ref == null) {
            return null;
        }
        final Context c = ref.get();
        if (c == null) {
            contextObjects.remove(className);
        }
        return c;
    }

    public synchronized void setActiveContext(Class<? extends BaseActivity> className, Context context) {
        WeakReference<Context> ref = new WeakReference<Context>(context);
        this.contextObjects.put(className, ref);
    }

    public synchronized Context getLastContext() {
        ArrayList<Class<? extends BaseActivity>> templList = new ArrayList<Class<? extends BaseActivity>>(
                contextObjects.keySet());
        for (ListIterator<Class<? extends BaseActivity>> it = templList.listIterator(templList.size()); it
                .hasPrevious(); ) {
            Object key = it.previous();
            WeakReference<Context> ref = contextObjects.get(key);
            if (ref == null) {
                return null;
            }
            final Context c = ref.get();
            if (c == null) {
                contextObjects.remove(key);
                continue;
            }
            return c;
        }
        return null;
    }

    public synchronized void resetActiveContext(Class<? extends BaseActivity> className) {
        contextObjects.remove(className);
    }
}
