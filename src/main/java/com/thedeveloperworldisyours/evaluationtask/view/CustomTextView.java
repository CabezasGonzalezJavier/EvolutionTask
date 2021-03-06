package com.thedeveloperworldisyours.evaluationtask.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.thedeveloperworldisyours.evaluationtask.MainApplication;
import com.thedeveloperworldisyours.evaluationtask.R;

/**
 * Created by javiergonzalezcabezas on 8/5/15.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    /**
     * Init type font
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewForm);
            setTypeface(MainApplication.Fonts.PENCIL);
            a.recycle();
        }
    }
}

