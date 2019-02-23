package com.githubpr.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.githubpr.R;
import com.githubpr.data.BaseData;
import com.githubpr.data.DataModel;
import com.githubpr.data.LoaderData;
import com.githubpr.ui.viewholder.BaseViewHolder;
import com.githubpr.ui.viewholder.LoaderViewHolder;
import com.githubpr.ui.viewholder.PRListViewHolder;

import java.util.List;

public class PRListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADER = 1;

    private static final int VIEW_TYPE_DATA = 2;
    private Activity context;

    private List<BaseData> dataList;

    public PRListAdapter(Activity context, List<BaseData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_LOADER) {
            view = context.getLayoutInflater().inflate(R.layout.pr_loader_item, viewGroup, false);
            return new LoaderViewHolder(view);
        }
        view = context.getLayoutInflater().inflate(R.layout.pr_list_item, viewGroup, false);
        return new PRListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        if (viewHolder instanceof LoaderViewHolder)
            return;
        ((PRListViewHolder) viewHolder).bindData((DataModel) dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof LoaderData)
            return VIEW_TYPE_LOADER;
        return VIEW_TYPE_DATA;
    }

    public void updateData(List<BaseData> dataList) {
        this.dataList = dataList;
    }
}
