package com.ecs160.socialMediaPost;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String postId;
    private String postContent;
    private List<Reply> replies;

    // constructor
    public Post(String postId, String postContent, List<Reply> replies) {
        this.postId = postId;
        this.postContent = postContent;
        this.replies = replies;
    }

    // getters and setters
    public String getPostId() { return postId; }
    public String getPostContent() { return postContent; }
    public List<Reply> getReplies() { return replies; }
    public void setReplies(List<Reply> replies) {this.replies = replies;}
}