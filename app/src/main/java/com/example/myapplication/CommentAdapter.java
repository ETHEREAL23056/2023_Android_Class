package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private static List<Comment> commentList = null;
    private static String username = null;
    private final PostDatabaseHelper dbHelper;
    private CommentAdapter.OnItemClickListener listener;

    public static Comment getItem(int position) {
        return commentList.get(position);
    }
    public static List<Comment> getCommentList(){return CommentAdapter.commentList;}

    public interface OnItemClickListener {
        void onButtonClick(int position);
    }

    public CommentAdapter(List<Comment> commentList, String username, PostDatabaseHelper dbHelper) {
        CommentAdapter.commentList = commentList;
        CommentAdapter.username = username;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    // 删除评论
    public void deleteComment(int position) {
        Comment deletedComment = commentList.get(position);
        commentList.remove(position);
        notifyItemRemoved(position);
        dbHelper.deleteComment(deletedComment.getCommentId());
    }
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewContent;
        private final Button button;
        // 设置左滑显示按钮
        public CommentViewHolder(@NonNull View itemView, final CommentAdapter.OnItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            button = itemView.findViewById(R.id.button);

            itemView.setOnTouchListener(new View.OnTouchListener() {
                private float initialX;

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = event.getX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float currentX = event.getX();
                            float deltaX = initialX - currentX;
                            if (deltaX > button.getWidth() * 0.2) {
                                button.setVisibility(View.VISIBLE);
                            } else {
                                button.setVisibility(View.GONE);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            button.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onButtonClick(position);
                        }
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(Comment comment) {
            textViewTitle.setText("User:" + comment.getUsername());
            textViewContent.setText(comment.getText());
        }

    }
}
