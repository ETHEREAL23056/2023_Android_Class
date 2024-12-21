package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements CommentAdapter.OnItemClickListener {

    EditText editText;
    PostDatabaseHelper dbHelper;
    Post post;
    List<Comment> commentList;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    String username;
    @SuppressLint("SetTextI18n")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取上一个界面传递过来的帖子名称
        String postTitle = getIntent().getStringExtra("title");
        username = getIntent().getStringExtra("username");
        // 从数据库中获取帖子内容和评论
        dbHelper = new PostDatabaseHelper(this);
        post = dbHelper.getPostByTitle(postTitle);
        String postId = post.getPostId();
        String author = post.getAuthor();

        TextView postTitleTextView, postAuthorTextView, postContentTextView;
        // 判断是否为创建用户
        if(author.equals(username)){
            setContentView(R.layout.activity_detail_yes);
            postTitleTextView = findViewById(R.id.postTitleTextView);
            postAuthorTextView = findViewById(R.id.postAuthorTextView);
            postContentTextView = findViewById(R.id.postContentTextView);
            Button backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            // 增加删除按钮
            Button deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog();
                }
            });
        }else{
            setContentView(R.layout.activity_detail_no);
            postTitleTextView = findViewById(R.id.postTitleTextView);
            postAuthorTextView = findViewById(R.id.postAuthorTextView);
            postContentTextView = findViewById(R.id.postContentTextView);
            Button backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
        }
        // 显示帖子信息和回复内容
        String postContent = post.getContent();
        commentList = dbHelper.getCommentsForPost(postId);
        postTitleTextView.setText(postTitle);
        postAuthorTextView.setText("Posted By " + author);
        postContentTextView.setText(postContent);
        // 设置RecyclerView的内容
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList, username, dbHelper);
        commentAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(commentAdapter);
        // 设置添加按钮
        FloatingActionButton floatingActionButton = findViewById(R.id.fabAddPost);
        floatingActionButton.setOnClickListener(new onClickListener());
        editText = findViewById(R.id.editTextComment);
    }

    @Override
    public void onButtonClick(int position) {
        List<Comment> comments = CommentAdapter.getCommentList();
        if(comments.get(position).getUsername().equals(username)){
            showDelete(position);
        }else{
            showAlert("没有权限");
        }
    }
    private void showDelete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commentAdapter.deleteComment(position);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    public class onClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String content = editText.getText().toString();
            if (content.isEmpty()) {
                showAlert("不能添加空白内容");
                return;
            }
            if (dbHelper.addComment(post.getPostId(), content, username)) {
                showAlert("评论成功");
                editText.setText("");
            }
            refreshCommentList();
        }
    }
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    // 数据库显示刷新操作
    private void refreshCommentList(){
        List<Comment> comments = dbHelper.getCommentsForPost(post.getPostId());
        commentList.clear();
        commentList.addAll(comments);
        commentAdapter = new CommentAdapter(commentList, username, dbHelper);
        recyclerView.setAdapter(commentAdapter);
    }
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void delete() {
        dbHelper.deletePostAndComments(post.getPostId());
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

}
