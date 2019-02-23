package com.meesho.githubpr.picasso;

import android.text.TextUtils;
import android.widget.ImageView;

import com.meesho.githubpr.app.MeeshoApp;
import com.squareup.picasso.Picasso;

public class ImageLoader {

    public static void displayImage(String url, ImageView imageView, int placeholderResId, int errorResId) {
        if (imageView == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(errorResId);
            return;
        }

        try {
            Picasso.with(MeeshoApp.getInstance().getApplicationContext())
                    .load(url)
                    .placeholder(placeholderResId)
                    .error(errorResId)
                    .into(imageView);
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
        }
    }
}
