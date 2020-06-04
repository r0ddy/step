package com.google.sps.data;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private String datePosted;

    /**
     * Constructor to use when receiving input from user.
     */
    protected Comment(String text){
        this.text = text;
        this.datePosted = convertDateToString(new Date());
    }

    /**
     * Constructor to use when receiving input from Datastore.
     */
    protected Comment(Long id, String text, String datePosted){
        this.id = id;
        this.text = text;
        this.datePosted = datePosted;
    }

    /**
     * Helper function to turn Date object into
     * String using dd/mm/yyyy format.
     * @param date The date object to convert.
     * @return The String representation.
     */
    private static String convertDateToString(Date date){
        DateFormat monthDayYear = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = monthDayYear.format(date);
        return dateString;
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
