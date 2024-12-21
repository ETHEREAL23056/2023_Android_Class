package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class UserPostsActivity extends AppCompatActivity implements PostAdapter.OnItemClickListener {
    private String username;
    private List<Post> postList;
    private PostDatabaseHelper postDatabaseHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    public UserPostsActivity() {}
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creates);
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        username = bundle.getString("username");

        Button backButton = findViewById(R.id.backButton);
        // 设置返回按钮点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        // 显示所有的评论
        postList = new ArrayList<>();
        postDatabaseHelper = new PostDatabaseHelper(this);
        List<Post> posts = postDatabaseHelper.getPostsByAuthor(username);
        postList.addAll(posts);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PostAdapter postAdapter = new PostAdapter(postList);
        postAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(postAdapter);
        // 设置刷新按钮的逻辑
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataFromPostDatabase();
            }
        });
    }

    private void fetchDataFromPostDatabase() {
        // 重新加载数据到RecyclerView
        List<Post> posts = postDatabaseHelper.getPostsByAuthor(username);
        postList.clear();
        postList.addAll(posts);
        // 停止刷新动画
        swipeRefreshLayout.setRefreshing(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onItemClick(int position) {
        Post clickedPost = PostAdapter.getItem(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", clickedPost.getTitle());
        intent.putExtra("content", clickedPost.getContent());
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}


