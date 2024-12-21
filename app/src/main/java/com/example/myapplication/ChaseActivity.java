package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChaseActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword, editTextPasswordRe;
    Button buttonUpdate, buttonBack;
    UserDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chase_password);

        databaseHelper = new UserDatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordRe = findViewById(R.id.editTextPasswordRe);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonBack = findViewById(R.id.buttonBack);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String password_re = editTextPasswordRe.getText().toString().trim();
                if(username.isEmpty() || password.isEmpty() || password_re.isEmpty()){
                    showAlert("请输入用户名和密码");
                    return;
                }
                if(! databaseHelper.isUsernameExist(username)){
                    showAlert("用户名不存在");
                    return;
                }else if(! password.equals(password_re)){
                    showAlert("两次输入的密码不一致");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ChaseActivity.this);
                builder.setMessage("确定要修改吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (databaseHelper.updateUserPassword(username, password)) {
                                    showAlert("修改成功");
                                    Intent intent = new Intent(ChaseActivity.this, LoginRegisterActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showAlert("修改错误");
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 跳转回到上一级界面
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    // 显示需要的信息，只有一个确定按钮
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
