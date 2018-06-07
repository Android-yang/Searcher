package com.android.yangke.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yangke.R;
import com.vondear.rxtools.view.dialog.RxDialog;

/**
 * author: yangke on 6/7/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 图片dialog
 * 样式：中间为图片，右上角为关闭按钮
 */
public class ImageDialog extends RxDialog {
    private ImageView mIvPhoto, mIvClose;
    private TextView mTxtDescription;
    private View dialog_view;

    public ImageDialog(Context context, int themeResId) {
        super(context, themeResId);
        iniView();
    }

    private void iniView() {
        dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_image, null);
        mIvPhoto = (ImageView) dialog_view.findViewById(R.id.iv_photo);
        mIvClose = (ImageView) dialog_view.findViewById(R.id.iv_close);
        mTxtDescription = (TextView) dialog_view.findViewById(R.id.txt_description);
        setContentView(dialog_view);
        closeDialog();
    }

    /**
     * @return 展示内容的imageView
     */
    public ImageView getIvContent() {
        return mIvPhoto;
    }

    public ImageDialog visibleView(int id) {
        View view = dialog_view.findViewById(id);
        if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public ImageDialog setDescription(String txt) {
        mTxtDescription.setText(txt);
        return this;
    }

    public void closeDialog() {
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
