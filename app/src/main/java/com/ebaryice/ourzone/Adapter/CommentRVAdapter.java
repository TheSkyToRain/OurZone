package com.ebaryice.ourzone.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebaryice.ourzone.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ebaryice on 2017/11/10.
 */

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.MyViewHolder> {

    private List<String> comments;

    public CommentRVAdapter(List<String> comments){
        this.comments = comments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.comment_item.setText(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_comment)
        TextView comment_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
