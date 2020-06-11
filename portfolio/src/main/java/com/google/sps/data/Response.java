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
    public Response(String userNickname, String text, Long parentId){
        super(userNickname, text);
        this.parentId = parentId;
    }

    /**
     * Constructor to use if input is from Datastore.
     */
    public Response(Entity responseEntity){
        super(responseEntity);
        this.parentId = (Long) responseEntity.getProperty("parentId");;
    }

    @Override
    protected Entity getEntity(){
        Entity commentEntity = super.getEntity();
        commentEntity.setProperty("parentId", this.parentId);
        return commentEntity;
    }
}
