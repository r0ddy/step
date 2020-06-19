package com.google.sps.data;

public class CommentBuilder {
    /**
     * The unique id value created by Datastore.
     */
    private Long id = null;
    
    /**
     * The text the user wrote.
     */
    private String text = null;
    
    /**
     * The date the user wrote the message in milliseconds.
     */
    private Long datePosted = null;

    /**
     * The user id of the user who posted this comment.
     */
    private String userNickname = null;

    /**
     * The URL of the image associated with this comment.
     */
    private String imageUrl = null;

    /**
     * The id of the comment that this 
     * is a child of.
     */
    private Long parentId = null;

    public CommentBuilder(String userNickname, String text){
        this.userNickname = userNickname;
        this.text = text;
        this.datePosted = System.currentTimeMillis();
    }

    public CommentBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public CommentBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public CommentBuilder setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public CommentBuilder setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public CommentBuilder setParentId(long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Comment build() {
        return new Comment(this.id, this.text, this.datePosted, this.userNickname, this.imageUrl, this.parentId);
    }
}