package com.google.sps.data;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.google.appengine.api.datastore.Entity;

public class Comment {
    private Long id;
    private String text;
    private String datePosted;

    protected Comment(String text){
        this.text = text;
        this.datePosted = convertDateToString(new Date());
    }

    protected Comment(Long id, String text, String datePosted){
        this.id = id;
        this.text = text;
        this.datePosted = datePosted;
    }

    private static String convertDateToString(Date date){
        DateFormat monthDayYear = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = monthDayYear.format(date);
        return dateString;
    }

    protected Entity getEntity(){
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", this.text);
        commentEntity.setProperty("datePosted", this.datePosted);
        return commentEntity;
    }
}
