package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PostDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "posts.db";
    public static final int DATABASE_VERSION = 1;

    // 帖子表
    public static final String TABLE_POSTS = "posts";
    public static final String COLUMN_POST_ID = "id";
    public static final String COLUMN_POST_TITLE = "title";
    public static final String COLUMN_POST_CONTENT = "content";

    // 评论表
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_COMMENT_ID = "id";
    public static final String COLUMN_COMMENT_POST_ID = "post_id";
    public static final String COLUMN_COMMENT_CONTENT = "content";
    public static final String COLUMN_POST_AUTHOR = "author";
    public PostDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建帖子表
        String createPostsTable = "CREATE TABLE " + TABLE_POSTS + " (" +
                COLUMN_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_POST_TITLE + " TEXT," +
                COLUMN_POST_CONTENT + " TEXT," +
                COLUMN_POST_AUTHOR + " TEXT)";
        db.execSQL(createPostsTable);

        // 创建评论表
        String createCommentsTable = "CREATE TABLE " + TABLE_COMMENTS + " (" +
                COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_COMMENT_POST_ID + " INTEGER," +
                COLUMN_COMMENT_CONTENT + " TEXT," +
                COLUMN_POST_AUTHOR + " TEXT)";
        db.execSQL(createCommentsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // 添加帖子
    public boolean addPost(String title, String content, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_TITLE, title);
        values.put(COLUMN_POST_CONTENT, content);
        values.put(COLUMN_POST_AUTHOR, author);
        db.insert(TABLE_POSTS, null, values);
        db.close();
        return true;
    }

    // 添加评论
    public boolean addComment(String postId, String content, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT_POST_ID, postId);
        values.put(COLUMN_COMMENT_CONTENT, content);
        values.put(COLUMN_POST_AUTHOR, author);
        db.insert(TABLE_COMMENTS, null, values);
        db.close();
        return true;
    }

    // 根据帖子标题获取帖子信息
    public Post getPostByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTS,
                null,
                COLUMN_POST_TITLE + "=?",
                new String[]{title},
                null, null, null);
        Post post = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_POST_ID));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(COLUMN_POST_CONTENT));
            @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(COLUMN_POST_AUTHOR)); // 获取作者信息
            post = new Post(id, title, content, author);
        }
        cursor.close();
        db.close();
        return post;
    }

    // 获取指定作者的所有帖子
    public List<Post> getPostsByAuthor(String author) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> posts = new ArrayList<>();
        Cursor cursor = db.query(TABLE_POSTS,
                null,
                COLUMN_POST_AUTHOR + "=?",
                new String[]{author},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Post post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return posts;
    }

    // 判断是否重复
    public boolean isDuplicateTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                PostDatabaseHelper.TABLE_POSTS,
                new String[]{PostDatabaseHelper.COLUMN_POST_TITLE},
                PostDatabaseHelper.COLUMN_POST_TITLE + "=?",
                new String[]{title},
                null, null, null);
        boolean isDuplicate = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isDuplicate;
    }

    // 获取所有帖子
    public List<Post> getAllPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> posts = new ArrayList<>();
        Cursor cursor = db.query(PostDatabaseHelper.TABLE_POSTS,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Post post = cursorToPost(cursor);
            posts.add(post);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return posts;
    }

    // 根据帖子编号获取评论
    public List<Comment> getCommentsForPost(String postId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor_user = db.query(PostDatabaseHelper.TABLE_POSTS,
                new String[]{PostDatabaseHelper.COLUMN_POST_AUTHOR},
                PostDatabaseHelper.COLUMN_POST_ID + "= ?",
                new String[]{postId},
                null, null, null);
        cursor_user.moveToFirst();
        String username = cursor_user.getString(0);
        cursor_user.close();

        List<Comment> comments = new ArrayList<>();
        Cursor cursor = db.query(PostDatabaseHelper.TABLE_COMMENTS,
                new String[]{ PostDatabaseHelper.COLUMN_COMMENT_ID,PostDatabaseHelper.COLUMN_COMMENT_CONTENT, PostDatabaseHelper.COLUMN_POST_ID, PostDatabaseHelper.COLUMN_POST_AUTHOR},
                PostDatabaseHelper.COLUMN_COMMENT_POST_ID + " = ?",
                new String[]{postId},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = new Comment(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3));
            comments.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return comments;
    }

    // 删除帖子和对应的评论
    public void deletePostAndComments(String postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMENTS, COLUMN_COMMENT_POST_ID + " = ?", new String[]{postId});
        db.delete(TABLE_POSTS, COLUMN_POST_ID + " = ?", new String[]{postId});
        db.close();
    }

    // 删除一条评论
    public void deleteComment(String commentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMENTS, COLUMN_COMMENT_ID + " = ?", new String[]{commentId});
        db.close();
    }

    // 将Cursor对象转换为Post对象
    private Post cursorToPost(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(COLUMN_POST_ID);
        int titleIndex = cursor.getColumnIndex(COLUMN_POST_TITLE);
        int contentIndex = cursor.getColumnIndex(COLUMN_POST_CONTENT);
        int authorIndex = cursor.getColumnIndex(COLUMN_POST_AUTHOR); // 添加作者索引
        String id = cursor.getString(idIndex);
        String title = cursor.getString(titleIndex);
        String content = cursor.getString(contentIndex);
        String author = cursor.getString(authorIndex); // 获取作者信息
        return new Post(id, title, content, author);
    }

}
