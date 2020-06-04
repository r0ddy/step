package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Comment {
    public static List<Comment> COMMENTS = new ArrayList<>();
    
    private int id;
    private String text;
    private Date date_posted;
    private Integer parent_id;

    private Comment(String text){
        this.id = COMMENTS.size();
        this.text = text;
        this.date_posted = new Date();
    }

    private Comment(String text, int parent_id){
        this(text);
        this.parent_id = parent_id;
    }

    public static void AddComment(String text){
        COMMENTS.add(new Comment(text));
    }

    public static void AddResponse(String text, int id){
        COMMENTS.add(new Comment(text, id));
    }
}