package com.example.model;

import java.util.List;

public class Tweet {
    String account;
    String post_time;
    String post_text;
    String post_link;

    int reply;
    String post_id;
    int retweet;
    int like;

    List<String> tags;
    List<String> hashtags;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_link() {
        return post_link;
    }

    public void setPost_link(String post_link) {
        this.post_link = post_link;
    }

    public void setRetweet(int retweet) {
        this.retweet = retweet;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getAccount() {
        return account;
    }

    public String getPost_time() {
        return post_time;
    }

    public String getPost_text() {
        return post_text;
    }

    public int getReply() {
        return reply;
    }

    public int getRetweet() {
        return retweet;
    }

    public int getLike() {
        return like;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getHashtags() {
        return hashtags;
    }
    
 // Thêm constructor mặc định
    public Tweet() {
    }

    public Tweet(String account, String post_time, String post_text, int reply, int retweet, int like) {
        this.account = account;
        this.post_time = post_time;
        this.post_text = post_text;
        this.reply = reply;
        this.retweet = retweet;
        this.like = like;
    }

    public static Tweet createTweet(String account, String timeStamp, String tweetText, int reply, int retweet,
            int like) {
        return new Tweet(account, timeStamp, tweetText, reply, retweet, like);
    }

    public Tweet(String account, String post_id, String post_time, String post_link, int reply, int retweet, int like) {
        this.account = account;
    }
    
    @Override
    public String toString() {
        return "Account: " + this.account;
    }
}

