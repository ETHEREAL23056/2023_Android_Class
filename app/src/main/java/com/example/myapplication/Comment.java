package com.example.myapplication;

public class Comment {
    private String commentId;
    private String text;
    private String userId;
    private String username;
    public Comment() {}
    public Comment(String commentId, String text, String userId, String username) {
        this.commentId = commentId;
        this.text = text;
        this.userId = userId;
        this.username = username;
    }

    // 获取评论内容
    public String getText() {
        return text;
    }

    // 设置评论内容
    public void setText(String text) {
        this.text = text;
    }

    // 获取评论ID
    public String getCommentId() {
        return commentId;
    }

    // 获取评论用户ID
    public String getUserId() {
        return this.userId;
    }
    // 获取评论用户用户名
    public String getUsername(){return this.username;}

}

