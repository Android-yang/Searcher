package com.android.yangke.base;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.http.ListenerMatcher;
import com.android.yangke.http.NetworkTask;
import com.android.yangke.http.RequestListener;
import com.android.yangke.http.SimpleRequestListenerSwitcher;
import com.android.yangke.http.TaskManager;
import com.android.yangke.tool.Constant;
import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 标题栏和状态栏的抽象
 */
public abstract class AbsActivity extends SwipeBackActivity implements RequestListener {

    public Toolbar mToolbar;//整体标题栏容器
    private TextView mTitleRight;//右标题
    private FrameLayout mContentContainerView;//存放内容容器
    private View mToolbarLine;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onHandleMessage(msg);
        }
    };
    private SimpleRequestListenerSwitcher mSwitcher = new SimpleRequestListenerSwitcher();
    private AlertDialog progressDialog;
    protected int progressClient = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        RxDeviceTool.setPortrait(this);
    }

    @Override
    public void setContentView(int resId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View content = inflater.inflate(resId, null);
        setContentView(content);
    }

    @Override
    public void setContentView(View view) {
        mContentContainerView = (FrameLayout) findViewById(R.id.activity_content);
        mContentContainerView.removeAllViewsInLayout();
        mContentContainerView.addView(view);

        initTitleBar();
    }

    /**
     * 重写此方法可解决子类重写 setSupportActionBar 时，先前设置的功能失效问题。
     * 例：标题左边点击事件失效
     *
     * @param toolbar
     */
    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mToolbar.setNavigationOnClickListener(leftBtnOnClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMethodName();
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMethodName();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMethodName();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMethodName();
    }

    /**
     * initialize title
     */
    private void initTitleBar() {
        mToolbar = (Toolbar) findViewById(R.id.base_toolbar);
        mToolbar.setNavigationOnClickListener(leftBtnOnClickListener);
        mToolbar.setNavigationIcon(R.drawable.title_back);

        ImmersionBar.setTitleBar(this, mToolbar);

        mTitleRight = (TextView) findViewById(R.id.title_right);
        mToolbarLine = findViewById(R.id.base_toolbar_line);
        mTitleRight.setOnClickListener(rightBtnOnClickListener);
    }


    public void setTitleRight(int resId) {
        if (resId > 0) {
            setTitleRight(getString(resId));
        }
    }

    /**
     * ToolBar 下面展示横线
     */
    public void setToolbarLineVisible() {
        mToolbarLine.setVisibility(View.VISIBLE);
    }

    public void setTitleRight(CharSequence title) {
        mTitleRight.setText(title);
        mTitleRight.setVisibility(View.VISIBLE);
    }

    public void setTitleRight(int resId, Drawable drawable) {
        if (resId > 0) {
            setTitleRight(getString(resId), drawable);
        }
    }

    public void setTitleRight(CharSequence title, Drawable drawable) {
        mTitleRight.setText(title);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTitleRight.setCompoundDrawables(null, null, drawable, null);
            mTitleRight.setCompoundDrawablePadding(8);
        }
        mTitleRight.setVisibility(View.VISIBLE);
    }

    public void hideTitleRight() {
        mTitleRight.setVisibility(View.GONE);
    }

    public void hideFinishBtn() {
        mToolbar.setNavigationIcon(null);
    }

    public void hideTitleBar() {
        mToolbar.setVisibility(View.GONE);
    }

    public void setTileLeft(String title) {
        mToolbar.setTitle(title);
    }

    public void showTitleBar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected View.OnClickListener leftBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onLeftButtonClick();
        }
    };

    View.OnClickListener rightBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRightButtonClick();
        }
    };

    protected void onRightButtonClick() {
    }

    protected void onLeftButtonClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMethodName();
        closeListener();
        Application application = getApplication();
        if (application instanceof BaseApplication) {
            ((BaseApplication) application).resetActiveContext(getClass());
        }
        TaskManager.getInstance().cancelTasks(new ListenerMatcher(this));
        logMethodName();
    }


    private void logMethodName() {
        if (Constant.INSTANCE.getLOG_LIFECYCLE()) {
            String methodName = new Throwable().getStackTrace()[1].getMethodName();
            String className = getClass().getSimpleName();
            Logger.d(className + " " + methodName + "()");
        }
    }


    @Override
    public void onDataReceivedSuccess(List<?> list) {

    }

    @Override
    public void onDataReceiveFailed() {

    }

    @Override
    public void onRequestStart(NetworkTask task) {
        if (task.isBlock()) {
            onShowProgress(task);
        }
    }

    @Override
    public void onRequestEnd(NetworkTask task) {
        if (task.isBlock()) {
            onCloseProgress(task);
        }
    }

    @Override
    public void onNetError(NetworkTask task) {
        if ((task.isBlock()) || (task.isShowError())) {
            RxToast.warning(getApplicationContext().getString(R.string.network_failed));
        }
    }

    @Override
    public void onRequestSuccess(NetworkTask task) {
    }

    @Override
    public void onRequestFail(NetworkTask task) {
    }

    @Override
    public void onFail(NetworkTask task) {
    }

    @Override
    public void openListener() {
        mSwitcher.openListener();
    }

    @Override
    public void closeListener() {
        mSwitcher.closeListener();
    }

    @Override
    public boolean isListenerOn() {
        return mSwitcher.isListenerOn();
    }

    public void onCloseProgress(NetworkTask networkParam) {
        onCloseProgress(networkParam.toString());
    }

    public void onCloseProgress(String message) {
        closeProgress();
    }

    public void onShowProgress(final NetworkTask networkParam) {
        if (isFinishing()) {
            return;
        }
        if (this.progressDialog == null) {
            Activity current = this;
            if (getParent() != null) {
                current = getParent();
            }
            if (current == null) {
                current = this;
            }
            Window window = current.getWindow();
            if (window != null) {
                window.makeActive();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("努力加载中...");
            this.progressDialog = builder.show();
            this.progressDialog.setCancelable(false);
            this.progressDialog.setMessage(networkParam.getLoadingMessage());
            this.progressDialog.setCancelable(networkParam.isCancelable());
            this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    progressClient = 0;
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    progressDialog = null;
                    networkParam.cancel();
                    onNetProgressCancel();
                    if (networkParam.isReturnOnCancel()) {
                        onBackPressed();
                    }
                }
            });
            this.progressClient++;
        } else {
            this.progressClient++;
        }
    }

    public void onNetProgressCancel() {
    }

    public void closeProgress() {
        if (this.progressDialog == null) {
            this.progressClient = 0;
            return;
        }
        this.progressClient--;
        if (this.progressClient <= 0) {
            this.progressClient = 0;
            if (this.progressDialog != null) {
                this.progressDialog.dismiss();
            }
            this.progressDialog = null;
        }
    }

    protected abstract void onHandleMessage(Message msg);

    private static long lastClickTime;

    /**
     * @return true 标识是快速点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 <= timeD && timeD < 200) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
