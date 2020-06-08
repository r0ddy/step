package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import java.util.ConcurrentModificationException;

/**
 * Represents a utility for creating, storing,
 * and retrieving Comment and Response objects.
 */
public final class CommentUtil {
    private static Logger logger = Logger.getLogger("com.google.sps.commentutil");

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

    public static void deleteAllComments(){
        int retries = 3;
        // Projected query for only comment ids
        Query query = new Query("Comment").setKeysOnly();

        // Begin transaction for deleting all comments
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Iterable<Entity> results = datastore.prepare(query).asIterable();
        TransactionOptions options = TransactionOptions.Builder.withXG(true);
        Transaction deleteAllTransaction = datastore.beginTransaction(options);
        List<Key> keys = new ArrayList<>();
        for(Entity entity : results){
            keys.add(entity.getKey());
        }
        while (true) {
            try {
                datastore.delete(deleteAllTransaction, keys);
                deleteAllTransaction.commit();
                break;
            } catch (ConcurrentModificationException e) {
                if (retries == 0) {
                    logger.log(Level.WARNING, "Cannot delete all comments", e);
                    break;
                }
                // Allow retry to occur
                --retries;
            } finally {
                if (deleteAllTransaction.isActive()) {
                    deleteAllTransaction.rollback();
                }
            }
        }
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
