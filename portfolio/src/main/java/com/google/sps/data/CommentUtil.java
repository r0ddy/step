package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;

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
    public static List<Comment> getComments(Integer numComments){
        Query query = new Query("Comment").addSort("datePosted", SortDirection.ASCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        Iterable<Entity> commentEntities;
        if(numComments == null){
            commentEntities = results.asIterable();
        }
        else{
            commentEntities = results.asIterable(FetchOptions.Builder.withLimit(numComments));
        }
        List<Comment> comments = new ArrayList<>();
        for (Entity entity : commentEntities) {
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
        Long datePosted = (Long) entity.getProperty("datePosted");
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
