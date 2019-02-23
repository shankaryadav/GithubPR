package com.githubpr.utils;

import android.content.Context;
import android.content.Intent;

import com.githubpr.ui.activity.PRListActivity;

public class IntentFactory {

    public static Intent getPRListActivity(Context context, String githubOwner, String githubRepo) {
        Intent intent = new Intent(context, PRListActivity.class);
        intent.putExtra(Constants.GITHUB_OWNER_NAME, githubOwner);
        intent.putExtra(Constants.GITHUB_REPO_NAME, githubRepo);
        return intent;
    }
}
