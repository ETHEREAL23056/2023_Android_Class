package com.example.myapplication;

import java.util.List;

public class Post {
    private String postId;
    private String author;
    private String title;
    private String content;
    private List<Comment> comments;

    public Post() {}

    public Post(String postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post(String postId, String title, String content, String author) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.author = author;
    }
    public String getAuthor(){return this.author;}

    // 获取帖子标题
    public String getTitle() {
        return title;
    }

    // 设置帖子标题
    public void setTitle(String title) {
        this.title = title;
    }

    // 获取帖子内容
    public String getContent() {
        return content;
    }

    // 设置帖子内容
    public void setContent(String content) {
        this.content = content;
    }

    // 获取帖子ID
    public String getPostId() {
        return postId;
    }

    // 设置帖子ID
    public void setPostId(String postId) {
        this.postId = postId;
    }

    // 获取帖子的所有评论
    public List<Comment> getComments() {
        return comments;
    }

    // 设置帖子的所有评论
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // 添加一条评论
    public void addComment(Comment comment) {
        comments.add(comment);
    }

}

