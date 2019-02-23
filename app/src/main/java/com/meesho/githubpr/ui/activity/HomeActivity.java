package com.meesho.githubpr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.meesho.githubpr.R;
import com.meesho.githubpr.utils.Constants;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {


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
        //ButterKnife.bind(this);
        setTitle(R.string.github);
        githubOwnerName = findViewById(R.id.github_owner_name);
        githubRepoName = findViewById(R.id.github_repo_name);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PRListActivity.class);
                intent.putExtra(Constants.GITHUB_OWNER_NAME, githubOwnerName.getText().toString());
                intent.putExtra(Constants.GITHUB_REPO_NAME, githubRepoName.getText().toString());
                startActivity(intent);
            }
        });
    }


    @OnClick(R.id.submit)
    public void onClickSubmit() {
        Intent intent = new Intent(this, PRListActivity.class);
        intent.putExtra(Constants.GITHUB_REPO_NAME, githubOwnerName.getText());
        intent.putExtra(Constants.GITHUB_REPO_NAME, githubRepoName.getText());
        startActivity(intent);
    }
}
