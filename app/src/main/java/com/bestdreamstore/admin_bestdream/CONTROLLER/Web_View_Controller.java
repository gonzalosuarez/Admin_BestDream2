package com.bestdreamstore.admin_bestdream.CONTROLLER;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.webkit.WebSettings;

import com.bestdreamstore.admin_bestdream.R;

import android.webkit.WebView;

public class Web_View_Controller {

    Context context;
    public static View view;
    ProgressDialog dialog;


    public Web_View_Controller(Context context){

        super();
        this.context = context;



    }



    public static void SHOW_POOP_UP_WEB_VIEW(final Context ctx, String url) {

        final PopupWindow mRecordWindow;

        /*INICIA POPUP*/


        View view = View.inflate(ctx, R.layout.poop_up_webview, null);
        mRecordWindow = new PopupWindow(view, -1, -1);
        mRecordWindow.showAtLocation(view, 17, 0, 0);
        mRecordWindow.setFocusable(true);
        mRecordWindow.setOutsideTouchable(false);
        mRecordWindow.setTouchable(false);

        ImageButton close = (ImageButton)view.findViewById(R.id.closePopupBtn);


        WebView mWebView = (WebView)view.findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl(url);


        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mRecordWindow.dismiss();

            }
        });




    }






}
