package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikesViewHolder> {

    private static List<Post> postList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public LikesAdapter(List<Post> postList) {
        LikesAdapter.postList = postList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LikesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new LikesViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LikesViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postTitle.setText(post.getTitle());
        holder.postTextView.setText(post.getContent());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static Post getItem(int position) {
        return postList.get(position);
    }

    static class LikesViewHolder extends RecyclerView.ViewHolder {
        TextView postTextView, postTitle;

        LikesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.textViewTitle);
            postTextView = itemView.findViewById(R.id.textViewContent);

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}

