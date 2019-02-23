package com.githubpr.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

public class PRViewerApp extends Application {

    private static PRViewerApp _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        initializePicasso();
    }

    public static PRViewerApp getInstance() {
        return _instance;
    }

    private void initializePicasso() {
        try {
            Picasso.Builder picassoBuilder = new Picasso.Builder(this);
            picassoBuilder.defaultBitmapConfig(Bitmap.Config.RGB_565);
            picassoBuilder.memoryCache(new LruCache(getApplicationContext()));
            Picasso picasso = picassoBuilder.build();
            Picasso.setSingletonInstance(picasso);
        } catch (Exception ex) {
        }
    }
}
