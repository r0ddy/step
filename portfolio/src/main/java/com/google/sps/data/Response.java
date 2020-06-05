package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/** 
 * Represents a child response to a comment or response.
 */
public final class Response extends Comment {

    /**
     * The id of the response or comment that this 
     * is a child of.
     */
    private Long parentId;
    
    /**
     * Constructor to use if input is from user.
     */
    public Response(String text, Long parentId){
        super(text);
        this.parentId = parentId;
    }

    /**
     * Constructor to use if input is from Datastore.
     */
    public Response(Long id, String text, Long datePosted, Long parentId){
        super(id, text, datePosted);
        this.parentId = parentId;
    }

    @Override
    protected Entity getEntity(){
        Entity commentEntity = super.getEntity();
        commentEntity.setProperty("parentId", this.parentId);
        return commentEntity;
    }
}
