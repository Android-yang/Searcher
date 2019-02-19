package com.android.yangke.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.android.yangke.R;

/**
 * author: yangke on 2018/11/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : loading view
 */
public class RxLoadingView extends Dialog {
    public RxLoadingView(@NonNull Context context) {
        super(context);
        iniView(context);
    }

    public RxLoadingView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        iniView(context);
    }

    public RxLoadingView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        iniView(context);
    }

    private void iniView(Context ctx) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.base_loading_view, null);
        setContentView(v);
        //set dialog background transparent
        getWindow().setDimAmount(0f);
    }
}

