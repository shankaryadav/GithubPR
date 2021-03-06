package com.githubpr.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.githubpr.R;
import com.githubpr.data.DataModel;
import com.githubpr.picasso.ImageLoader;

public class PRListViewHolder extends BaseViewHolder {

    private TextView number;

    private TextView title;

    private TextView user;

    private ImageView thumbnail;

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
