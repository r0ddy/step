package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/** 
 * Represents a comment on a blog or post.
 */
public class Comment {
    
    /**
     * The unique id value created by Datastore.
     */
    private Long id;
    
    /**
     * The text the user wrote.
     */
    private String text;
    
    /**
     * The date the user wrote the message in milliseconds.
     */
    private Long datePosted;

    /**
     * The user id of the user who posted this comment.
     */
    private String userNickname;

    /**
     * The URL of the image associated with this comment.
     */
    private String imageUrl;
    
    /**
     * The id of the comment that this 
     * is a child of.
     */
    private Long parentId;
    
    /**
     * Constructor used by CommentBuilder
     * @param id
     * @param text
     * @param datePosted
     * @param userNickname
     * @param imageUrl
     * @param parentId
     */
    protected Comment(Long id, String text, Long datePosted, String userNickname, String imageUrl, Long parentId){
        this.id = id;
        this.text = text;
        this.datePosted = datePosted;
        this.userNickname = userNickname;
        this.imageUrl = imageUrl;
        this.parentId = parentId;
    }

    /**
     * Constructor to use when receiving input from Datastore.
     */
    protected Comment(Entity commentEntity){
        this.id = commentEntity.getKey().getId();
        this.userNickname = (String) commentEntity.getProperty("userNickname");
        this.text = (String) commentEntity.getProperty("text");
        this.datePosted = (Long) commentEntity.getProperty("datePosted");
        this.imageUrl = (String) commentEntity.getProperty("imageUrl");
        this.parentId = (Long) commentEntity.getProperty("parentId");
    }

    /**
     * Creates an entity from this for storing in Datastore.
     * @return Entity to put into Datastore.
     * Override if you want to add more properties to the Entity.
     */
    protected Entity getEntity(){
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("userNickname", this.userNickname);
        commentEntity.setProperty("text", this.text);
        commentEntity.setProperty("datePosted", this.datePosted);
        commentEntity.setProperty("imageUrl", this.imageUrl);
        commentEntity.setProperty("parentId", this.parentId);
        return commentEntity;
    }
}
