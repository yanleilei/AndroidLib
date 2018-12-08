package com.leilei.androidlib;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by leilei on 2018/8/21.
 */
public class ToastUtil {

    private static Builder mDefaultBuilder;


    public static void setDefaultBuilder(Builder builder) {
        mDefaultBuilder = builder;
    }

    public static void toastLong(String message) {
        toast(message, null, Toast.LENGTH_LONG);
    }

    public static void toastLong(int resId) {
        toast(Utils.mContext.getResources().getString(resId), null, Toast.LENGTH_LONG);
    }

    public static void toastLong(int resId, Builder builder) {
        toast(Utils.mContext.getResources().getString(resId), builder, Toast.LENGTH_LONG);
    }

    public static void toastLong(String message, Builder builder) {
        toast(message, builder, Toast.LENGTH_LONG);
    }

    public static void toastShort(int resId) {
        toast(Utils.mContext.getResources().getString(resId), null, Toast.LENGTH_SHORT);
    }

    public static void toastShort(String message) {
        toast(message, null, Toast.LENGTH_SHORT);
    }

    public static void toastShort(int resId, Builder builder) {
        toast(Utils.mContext.getResources().getString(resId), builder, Toast.LENGTH_SHORT);
    }

    public static void toastShort(String msg, Builder builder) {
        toast(msg, builder, Toast.LENGTH_SHORT);
    }

    public static void toast(int resId) {
        toast(Utils.mContext.getResources().getString(resId), null, Toast.LENGTH_SHORT);
    }

    public static void toast(int resId, Builder builder) {
        toast(Utils.mContext.getResources().getString(resId), builder, Toast.LENGTH_SHORT);
    }

    public static void toast(int resId, Builder builder, int time) {
        toast(Utils.mContext.getResources().getString(resId), builder, time);
    }

    public static void toast(String message) {
        toast(message, null, Toast.LENGTH_SHORT);
    }

    public static void toast(String message, Builder builder) {
        toast(message, builder);
    }

    public static void toast(String message, Builder builder, int time) {
        TextView textView = (TextView) View.inflate(Utils.mContext, R.layout.lib_toast_textview, null);
        textView.setText(message);
        if (builder == null && mDefaultBuilder != null) {
            builder = mDefaultBuilder;
        }
        if (builder != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, builder.mTextSize);
            textView.setTextColor(builder.mTextColor);
            textView.setPadding(builder.mTextPadding, builder.mTextPadding, builder.mTextPadding, builder.mTextPadding);
            textView.setBackgroundResource(builder.mResBg);
        }

        Toast toast = new Toast(Utils.mContext);
        toast.setView(textView);
        toast.setDuration(time);
        if (builder != null) {
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, builder.mMarginBottom);
        } else {
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, ScreenUtils.dpTopx(100));
        }
        toast.show();
    }

    /*
    这个类用来配置自定义Toast的样式
     */
    public static class Builder {
        private int mTextSize = 14;
        private int mTextColor = Color.parseColor("#ffffff");
        private int mTextPadding = ScreenUtils.dpTopx(8);
        private int mResBg = R.drawable.lib_toast_bg;

        private int mMarginBottom = ScreenUtils.dpTopx(100);

        private Builder() {

        }

        public static Builder create() {
            return new Builder();
        }

        public Builder textSize(int textSize) {
            mTextSize = textSize;
            return this;
        }

        public Builder textColor(int textColor) {
            mTextColor = textColor;
            return this;
        }

        public Builder textPadding(int padding) {
            mTextPadding = padding;
            return this;
        }

        public Builder resBg(int resBg) {
            mResBg = resBg;
            return this;
        }

        public Builder marginBottom(int marginBottomDp) {
            mMarginBottom = ScreenUtils.dpTopx(marginBottomDp);
            return this;
        }
    }

}
