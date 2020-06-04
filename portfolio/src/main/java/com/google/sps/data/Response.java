package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public final class Response extends Comment {
    private Long parentId;
    
    public Response(String text, long parentId){
        super(text);
        this.parentId = parentId;
    }

    public Response(Long id, String text, String datePosted, Long parentId){
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
