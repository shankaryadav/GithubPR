package com.meesho.githubpr.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meesho.githubpr.R;
import com.meesho.githubpr.data.DataModel;
import com.meesho.githubpr.picasso.ImageLoader;

import butterknife.BindView;

public class PRListViewHolder extends BaseViewHolder {

    @BindView(R.id.pr_name)
    TextView number;

    @BindView(R.id.pr_number)
    TextView title;

    @BindView(R.id.pr_user)
    TextView user;

    @BindView(R.id.thumbnail)
    ImageView thumbnail;

    public PRListViewHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.pr_number);
        title = itemView.findViewById(R.id.pr_name);
        user = itemView.findViewById(R.id.pr_user);
        thumbnail = itemView.findViewById(R.id.thumbnail);
    }

    public void bindData(DataModel dataModel) {
        String prNumber = "#" + dataModel.getNumber() + "     User :";
        number.setText(prNumber);
        title.setText(dataModel.getTitle());
        user.setText(dataModel.getUser().getUserName());
        ImageLoader.displayImage(dataModel.getUser().getThumbnail(), thumbnail, R.drawable.ic_launcher_background, R.drawable.ic_error_thumb);
    }
}
