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
     * The date the user wrote the message in dd/mm/yyyy format.
     */
    private Long datePosted;

    /**
     * Constructor to use when receiving input from user.
     */
    protected Comment(String text){
        this.text = text;
        this.datePosted = System.currentTimeMillis();
    }

    /**
     * Constructor to use when receiving input from Datastore.
     */
    protected Comment(Long id, String text, Long datePosted){
        this.id = id;
        this.text = text;
        this.datePosted = datePosted;
    }

    /**
     * Creates an entity from this for storing in Datastore.
     * @return Entity to put into Datastore.
     * Override if you want to add more properties to the Entity.
     */
    protected Entity getEntity(){
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", this.text);
        commentEntity.setProperty("datePosted", this.datePosted);
        return commentEntity;
    }
}
