package com.example.shidehrahmanian.notebook;

/**
 * Created by shidehrahmanian on 2016-09-25.
 */

public class Note {
    private String title, message;
    private long noteId, dateCreatedMilli;
    private Category category;

    public enum Category {SCHOOL, WORK, OTHER }

    public Note(String title, String message, Category category){
        this.title=title;
        this.message=message;
        this.category=category;
        this.noteId=0;
        this.dateCreatedMilli=0;

    }

    public Note(String title, String message, Category category, long noteId, long dateCreatedMilli){
        this.title=title;
        this.message=message;
        this.category=category;
        this.noteId=noteId;
        this.dateCreatedMilli=dateCreatedMilli;
    }

    public String getTitle(){
        return  title;
    }

    public String getMessage(){
        return  message;
    }

    public long getDate(){ return dateCreatedMilli;}

    public long getId(){ return noteId;}

}
