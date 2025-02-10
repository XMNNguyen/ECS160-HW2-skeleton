package com.ecs160.socialMediaPost;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class customJsonParser {

    public List<Post> parsePosts(String filePath) throws Exception {
        List<Post> posts = new ArrayList<>();
        Gson gson = new Gson();

        JsonElement element = JsonParser.parseReader(new FileReader(filePath));

        if (!element.isJsonObject() || !element.getAsJsonObject().has("feed")) {
            System.err.println("Invalid JSON structure: Missing 'feed' key.");
            return posts;
        }

        JsonArray feedArray = element.getAsJsonObject().get("feed").getAsJsonArray();

        for (JsonElement feedElement : feedArray) {
            JsonObject feedObj = feedElement.getAsJsonObject();

            if (!feedObj.has("thread") || feedObj.get("thread").isJsonNull()) {
                System.out.println("Skipping feed element: Missing 'thread' key.");
                continue;
            }

            JsonObject thread = feedObj.get("thread").getAsJsonObject();

            if (!thread.has("post") || thread.get("post").isJsonNull()) {
                System.out.println("Skipping thread: Missing 'post' key.");
                continue;
            }

            JsonObject postObject = thread.get("post").getAsJsonObject();
            posts.add(parsePost(thread, postObject));
        }

        return posts;
    }


    private Post parsePost(JsonObject thread, JsonObject postObject) {
        try {
            // extract id and text
            String post_id = postObject.get("cid").getAsString();
            String text = postObject.get("record").getAsJsonObject().get("text").getAsString();
            List<Reply> replies = new ArrayList<>();

            Post post = new Post(post_id, text, replies);

            // if our post is not empty, attempt to extract replies
            if (post != null) {
                if (thread.has("replies") && !thread.get("replies").isJsonNull()) {
                    replies = parseReplies(thread.get("replies").getAsJsonArray(), post);
                    post.setReplies(replies);
                }
            }

            return post;
        } catch (Exception e) {
            System.err.println("Error parsing post: " + e.getMessage());
            return null;
        }
    }


    private List<Reply> parseReplies(JsonArray repliesArray, Post parentPost) {
        List<Reply> replies = new ArrayList<>();

        // iterate through replies and extract them
        for (JsonElement replyElement : repliesArray) {
            JsonObject replyObj = replyElement.getAsJsonObject();

            if (!replyObj.has("post") || replyObj.get("post").isJsonNull()) {
                System.out.println("Skipping reply: Missing 'post' key.");
                continue;
            }

            // extract id and text
            JsonObject postObject = replyObj.get("post").getAsJsonObject();
            String post_id = postObject.get("cid").getAsString();
            String text = postObject.get("record").getAsJsonObject().get("text").getAsString();


            Reply reply = new Reply(post_id, text, parentPost.getPostId());
            replies.add(reply);
        }

        return replies;
    }
}