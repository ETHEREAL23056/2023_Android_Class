package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static List<Post> postList = null;
    private OnItemClickListener listener;

    public static Post getItem(int position) {
        return postList.get(position);
    }

    public interface OnItemClickListener {
        @Nullable
        View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

        void onItemClick(int position);
    }
    public PostAdapter(List<Post> postList) {
        PostAdapter.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    public void setOnItemClickListener(PostAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewContent;

        public PostViewHolder(@NonNull View itemView, final PostAdapter.OnItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);

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

        @SuppressLint("SetTextI18n")
        public void bind(Post post) {
            textViewTitle.setText(post.getTitle());
            String content = post.getContent();
            if(content.length() < 50){
                textViewContent.setText(content);
            }else{
                textViewContent.setText(content.substring(0, 50) + "……");
            }
        }
    }

}

