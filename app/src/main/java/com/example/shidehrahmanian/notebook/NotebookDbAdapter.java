package com.example.shidehrahmanian.notebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shidehrahmanian on 2016-10-03.
 */

public class NotebookDbAdapter {

    private static final String DATABASE_NAME = "notebook.db";
    private static final int DATABASE_VERSION=1;

    public static final String NOTE_TABLE="note";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_MESSAGE="message";
    public static final String COLUMN_CATEGORY="category";
    public static final String COLUMN_DATE="date";

    private String[] allColumns={COLUMN_ID,COLUMN_TITLE,COLUMN_MESSAGE,COLUMN_CATEGORY, COLUMN_DATE};

    public static final String CREATE_TABLE_NOTE="create table " + NOTE_TABLE + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_MESSAGE + " text not null, "
            + COLUMN_CATEGORY + " integer not null "
            + COLUMN_DATE + ");";

    private SQLiteDatabase sqlDB;
    private Context context;

    private NotebookDbHelper notebookDbHelper;

    public NotebookDbAdapter (Context ctx){
        context=ctx;
    }

    public NotebookDbAdapter open() throws android.database.SQLException {
        notebookDbHelper=new NotebookDbHelper(context);
        sqlDB=notebookDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        notebookDbHelper.close();
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes= new ArrayList<Note>();

        Cursor cursor=sqlDB.query(NOTE_TABLE, allColumns, null, null, null, null, null);

        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
            Note note=cursorToNote(cursor);
            notes.add(note);
        }

        cursor.close();

        return notes;
    }

    private Note cursorToNote(Cursor cursor){
        Note newNote=new Note (cursor.getString(1), cursor.getString(2),
                Note.Category.valueOf(cursor.getString(3)), cursor.getLong(0), cursor.getLong(4));
        return newNote;
    }

    private static class NotebookDbHelper extends SQLiteOpenHelper{
        NotebookDbHelper (Context ctx){
            super (ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_TABLE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(NotebookDbHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
            onCreate(db);
        }
    }

}
