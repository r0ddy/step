package com.google.sps.data;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.google.appengine.api.datastore.Entity;

/** 
 * Represents a root response to a blog or post.
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
     * Constructor to use when receiving input from user.
     */
    protected Comment(String userNickname, String text, String imageUrl){
        this.userNickname = userNickname;
        this.text = text;
        this.datePosted = System.currentTimeMillis();
        this.imageUrl = imageUrl;
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
        return commentEntity;
    }
}
