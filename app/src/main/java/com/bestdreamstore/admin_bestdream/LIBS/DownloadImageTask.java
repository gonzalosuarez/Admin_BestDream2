package com.bestdreamstore.admin_bestdream.LIBS;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;

/**
 * Created by Sergey Shustikov (pandarium.shustikov@gmail.com) at 2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
    public static final int ANIMATION_DURATION = 250;
    private final ImageView mDestination, mFakeForError;
    private final String mUrl;
    private final ProgressBar mProgressBar;
    private Animation.AnimationListener mOutAnimationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation)
        {

        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {

        }
    };
    private Animation.AnimationListener mInAnimationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation)
        {
            if (isBitmapSet)
                mDestination.setVisibility(View.VISIBLE);
            else
                mFakeForError.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {

        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {

        }
    };
    private boolean isBitmapSet;

    public DownloadImageTask(Context context, ImageView destination, String url)
    {
        mDestination = destination;
        mUrl = url;
        ViewGroup parent = (ViewGroup) destination.getParent();
        mFakeForError = new ImageView(context);
        destination.setVisibility(View.GONE);
        FrameLayout layout = new FrameLayout(context);
        mProgressBar = new ProgressBar(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mProgressBar.setLayoutParams(params);
        FrameLayout.LayoutParams copy = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        copy.gravity = Gravity.CENTER;
        copy.width = dpToPx(48);
        copy.height = dpToPx(48);
        mFakeForError.setLayoutParams(copy);
        mFakeForError.setVisibility(View.GONE);
        mFakeForError.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        layout.addView(mProgressBar);
        layout.addView(mFakeForError);
        mProgressBar.setIndeterminate(true);
        parent.addView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected Bitmap doInBackground(String... urls)
    {
        String urlDisplay = mUrl;
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result)
    {
        AlphaAnimation in = new AlphaAnimation(0f, 1f);
        AlphaAnimation out = new AlphaAnimation(1f, 0f);
        in.setDuration(ANIMATION_DURATION * 2);
        out.setDuration(ANIMATION_DURATION);
        out.setAnimationListener(mOutAnimationListener);
        in.setAnimationListener(mInAnimationListener);
        in.setStartOffset(ANIMATION_DURATION);
        if (result != null) {
            mDestination.setImageBitmap(result);
            isBitmapSet = true;
            mDestination.startAnimation(in);
        } else {
            mFakeForError.startAnimation(in);
        }
        mProgressBar.startAnimation(out);
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mDestination.getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}