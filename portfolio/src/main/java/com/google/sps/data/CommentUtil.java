package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Represents a utility for creating, storing,
 * and retrieving Comment and Response objects.
 */
public final class CommentUtil {
    /**
     * Creates and stores a comment given a message.
     * @param text The comment's message.
     */
    public static void addComment(String text){
        Comment comment = new Comment(text);
        storeComment(comment);
    }

    /**
     * Creates and stores a response given a message and parent id.
     * @param text The response's message.
     * @param parentId The id of the parent response or comment.
     */
    public static void addResponse(String text, long parentId){
        Response response = new Response(text, parentId);
        storeComment(response);
    }

    /**
     * Retrieves all comments from the Datastore.
     * @return List of Comment objects retrieved from Datastore.
     */
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

    /**
     * Converts an Entity into a Comment or Response object.
     * Used to transform Entity object retrieved from Datastore.
     * @param entity Entity to transform.
     * @return The resulting Entity or Comment.
     */
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

    /**
     * Helper function for storing a Comment in Datastore.
     * @param comment Comment object to transform into entity and store.
     */
    private static void storeComment(Comment comment){
        Entity commentEntity = comment.getEntity();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
    }
}
