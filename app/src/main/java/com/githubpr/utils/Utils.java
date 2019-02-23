package com.githubpr.utils;

import android.content.Context;
import android.content.Intent;

public class Utils {

    /**
     * Control whole app startActivity by this method
     * @param context
     * @param intent
     */
    public static void startActivity(Context context, Intent intent) {
        if (context == null || intent == null)
            return;
        context.startActivity(intent);
    }
}
