package com.meesho.githubpr.ui.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meesho.githubpr.R;
import com.meesho.githubpr.data.DataModel;
import com.meesho.githubpr.picasso.ImageLoader;

public class PRListViewHolder extends BaseViewHolder {

    TextView number;

    TextView title;

    TextView user;

    ImageView thumbnail;

    public PRListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bindData(DataModel dataModel) {

        String prNumber = "#" + dataModel.getNumber() + "     User :";

        number.setText(prNumber);

        title.setText(dataModel.getTitle());

        user.setText(dataModel.getUser().getUserName());

        ImageLoader.displayImage(dataModel.getUser().getThumbnail(), thumbnail, R.drawable.ic_launcher_background, R.drawable.ic_error_thumb);
    }
}
