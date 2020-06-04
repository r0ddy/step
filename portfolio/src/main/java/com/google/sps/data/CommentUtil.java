package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public final class CommentUtil {
    public static void addComment(String text){
        Comment comment = new Comment(text);
        storeComment(comment);
    }

    public static void addResponse(String text, long parentId){
        Response response = new Response(text, parentId);
        storeComment(response);
    }

    public static List<Comment> getComments(){
        Query query = new Query("Comment");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        List<Comment> comments = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            Comment comment = convertEntityToComment(entity);
            comments.add(comment);
        }
        return comments;
    }

    private static Comment convertEntityToComment(Entity entity){
        long id = entity.getKey().getId();
        String text = (String) entity.getProperty("text");
        String datePosted = (String) entity.getProperty("datePosted");
        Long parentId = (Long) entity.getProperty("parentId");
        Comment comment;
        if(parentId == null){
            comment = new Comment(id, text, datePosted);
        }
        else{
            comment = new Response(id, text, datePosted, parentId);
        }
        return comment;
    }

    private static void storeComment(Comment comment){
        Entity commentEntity = comment.getEntity();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
    }
}
