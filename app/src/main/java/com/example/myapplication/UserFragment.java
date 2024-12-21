package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment implements LikesAdapter.OnItemClickListener{

    private TextView textViewUsername;
    private UserDatabaseHelper dbHelper;
    private final String username;

    public UserFragment(String username) {
        this.username = username;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewUsername.setText("User: " + this.username);

        Button buttonViewCreates =  view.findViewById(R.id.buttonViewCreates);
        buttonViewCreates.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UserPostsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    intent.putExtras(bundle);
                    startActivity(intent);
             }
        });

        Button buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordChangeDialog();
            }
        });

        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        dbHelper = new UserDatabaseHelper(getActivity());
        // 从数据库中获取用户名并显示
        loadUserData();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void loadUserData() {
        User user = dbHelper.getUser(username);
        if (user != null) {
            textViewUsername.setText("Username: " + user.getUsername());
        }
    }

    @Override
    public void onItemClick(int position) {
        Post clickedPost = LikesAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        // 传递帖子数据到新的Activity
        intent.putExtra("title", clickedPost.getTitle());
        intent.putExtra("content", clickedPost.getContent());
        startActivity(intent);
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void logout() {
        Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
        // 添加标志位，清除之前的所有 Activity，并创建新的登录界面
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
    private void showPasswordChangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout passwordChange = (LinearLayout)getLayoutInflater().inflate(R.layout.password_change,null);
        builder.setView(passwordChange);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText new_password = passwordChange.findViewById(R.id.new_password);
                        EditText new_re_password = passwordChange.findViewById(R.id.new_re_password);
                        String password = new_password.getText().toString().trim();
                        String re_password = new_re_password.getText().toString().trim();
                        if (!password.equals(re_password)) {
                            showAlert("请输入一致的新密码");
                            return;
                        }
                        if (dbHelper.updateUserPassword(username,password)) {
                            showAlert("修改成功");
                        } else {
                            showAlert("修改失败");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

}



