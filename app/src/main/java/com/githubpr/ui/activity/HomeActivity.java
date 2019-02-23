package com.githubpr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.githubpr.R;
import com.githubpr.utils.IntentFactory;
import com.githubpr.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {


    @BindView(R.id.github_owner_name)
    EditText githubOwnerName;

    @BindView(R.id.github_repo_name)
    EditText githubRepoName;

    @BindView(R.id.submit)
    TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setTitle(R.string.github);
    }


    @OnClick(R.id.submit)
    public void onClick() {
        Intent intent = IntentFactory.getPRListActivity(this, githubOwnerName.getText().toString(), githubRepoName.getText().toString());
        Utils.startActivity(this, intent);
    }
}
