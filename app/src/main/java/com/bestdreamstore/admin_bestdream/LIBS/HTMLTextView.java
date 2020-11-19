package com.bestdreamstore.admin_bestdream.LIBS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;


public class HTMLTextView extends android.support.v7.widget.AppCompatTextView {
    public HTMLTextView(Context context) {
        super(context);
    }
    public HTMLTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public HTMLTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        Spanned html = null;

        if(text != null) {
            html = Html.fromHtml(text.toString());
            super.setText(html, BufferType.SPANNABLE);
        } else {
            super.setText(text, type);
        }
    }
}