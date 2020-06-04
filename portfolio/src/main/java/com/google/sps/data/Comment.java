package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Comment {
    public static List<Comment> COMMENTS = new ArrayList<>();

    private Long id;
    private String text;
    private String datePosted;
    private Long parentId;

    private Comment(String text){
        this.text = text;
        this.datePosted = convertDateToString(new Date());
    }

    private Comment(String text, long parentId){
        this(text);
        this.parentId = parentId;
    }

    private Comment(long id, String text, String datePosted, Long parentId){
        this.id = id;
        this.text = text;
        this.datePosted = datePosted;
        this.parentId = parentId;
    }

    public static void AddComment(String text){
        Comment comment = new Comment(text);
        Entity commentEntity = convertCommentToEntity(comment);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
    }

    public static void AddResponse(String text, long parentId){
        Comment comment = new Comment(text, parentId);
        Entity commentEntity = convertCommentToEntity(comment);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
    }

    public static List<Comment> GetComments(){
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

    private static Entity convertCommentToEntity(Comment comment){
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", comment.text);
        commentEntity.setProperty("datePosted", comment.datePosted);
        if(comment.parentId != null){
            commentEntity.setProperty("parentId", comment.parentId);
        }
        return commentEntity;
    }

    private static Comment convertEntityToComment(Entity entity){
        long id = entity.getKey().getId();
        String text = (String) entity.getProperty("text");
        String datePosted = (String) entity.getProperty("datePosted");
        Long parentId = (Long) entity.getProperty("parentId");
        Comment comment = new Comment(id, text, datePosted, parentId);
        return comment;
    }

    private static String convertDateToString(Date date){
        DateFormat monthDayYear = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = monthDayYear.format(date);
        return dateString;
    }
}