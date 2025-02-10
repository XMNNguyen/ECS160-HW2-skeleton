package com.ecs160.socialMediaPost;

import com.ecs160.socialMediaPost.Post;
import java.util.List;

public class Reply extends Post {
    private String parent_id;

    // constructor
    public Reply(String postId, String postContent, String parent_id) {
        super(postId, postContent, null); // set replies to null since we are not checking replies in replies
        this.parent_id = parent_id;
    }

    // getters
    public String getParent_id() {return parent_id;}
}
