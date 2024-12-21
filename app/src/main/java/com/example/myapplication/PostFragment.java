package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener {
    private String username;
    private List<Post> postList;
    private PostDatabaseHelper postDatabaseHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PostFragment() {}
    public PostFragment(String username){
        this.username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.fabAddPost);
        floatingActionButton.setOnClickListener(new onClickListener());

        postList = new ArrayList<>();
        postDatabaseHelper = new PostDatabaseHelper(getActivity());
        List<Post> posts = postDatabaseHelper.getAllPosts();
        postList.addAll(posts);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PostAdapter postAdapter = new PostAdapter(postList);
        postAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(postAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作，重新加载数据
                fetchDataFromPostDatabase();
            }
        });

        return view;
    }

    private void fetchDataFromPostDatabase() {
        // 重新加载数据到RecyclerView
        List<Post> posts = postDatabaseHelper.getAllPosts();
        postList.clear();
        postList.addAll(posts);
        // 停止刷新动画
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(int position) {
        Post clickedPost = PostAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("title", clickedPost.getTitle());
        intent.putExtra("content", clickedPost.getContent());
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public class onClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CreatePostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("author", username);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
