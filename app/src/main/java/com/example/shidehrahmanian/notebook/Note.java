package com.example.shidehrahmanian.notebook;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shidehrahmanian on 2016-09-25.
 */

public class Note {
    private long noteId;
    private String title;
    private String message;

    public Note(long noteId, String title, String message){
        this.noteId=noteId;
        this.title=title;
        this.message=message;
    }


    public String getTitle(){
        return  title;
    }

    public String getMessage(){
        return  message;
    }

    public long getId() {return noteId;}

}


