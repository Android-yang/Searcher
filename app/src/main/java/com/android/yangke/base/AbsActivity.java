package com.android.yangke.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.yangke.R;
import com.gyf.barlibrary.ImmersionBar;
import com.vondear.rxtools.RxDeviceTool;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 标题栏和状态栏的抽象
 */
public class AbsActivity extends SwipeBackActivity {

    protected Toolbar mToolbar;//整体标题栏容器
    private TextView mTitleRight;//右标题
    private FrameLayout mContentContainerView;//存放内容容器
    private View mToolbarLine;

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
     * @param toolbar
     */
    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mToolbar.setNavigationOnClickListener(leftBtnOnClickListener);
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
    public void setToolbarLineVisible(){
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

}
