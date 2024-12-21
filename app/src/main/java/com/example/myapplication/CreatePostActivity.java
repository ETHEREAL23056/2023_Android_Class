package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePostActivity extends AppCompatActivity {
    PostDatabaseHelper postDatabaseHelper;
    private String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        postDatabaseHelper = new PostDatabaseHelper(this);
        EditText titleEditText = findViewById(R.id.editTextTitle);
        EditText contentEditText = findViewById(R.id.editTextContent);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        this.username = bundle.getString("author");

        // 设置返回按钮点击事件
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        // 设置添加帖子逻辑
        Button postButton = findViewById(R.id.buttonPost);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                if (title.isEmpty() || content.isEmpty()) {
                    showAlert("请输入标题和内容");
                    return;
                }

                if (postDatabaseHelper.isDuplicateTitle(title)) {
                    showAlert("标题已存在");
                    return;
                }
                boolean success = postDatabaseHelper.addPost(title, content, username);
                if (success) {
                    showAlert("发布成功");
                    Intent intent = new Intent(CreatePostActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    showAlert("发布失败");
                }
            }
        });
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

}

