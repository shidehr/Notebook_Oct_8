package com.example.shidehrahmanian.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by shidehrahmanian on 2016-10-03.
 */

public class NotebookDbAdapter {

    private static final String DATABASE_NAME = "notebookmy.db";
    private static final int DATABASE_VERSION=2;

    public static final String NOTE_TABLE="note";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_MESSAGE="message";


    private String[] allColumns={COLUMN_ID,COLUMN_TITLE,COLUMN_MESSAGE};

    public static final String CREATE_TABLE_NOTE="create table " + NOTE_TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_TITLE + " text not null, "
            + COLUMN_MESSAGE + " text not null " + ");";

    private SQLiteDatabase sqlDB;
    private Context context;

    private NotebookDbHelper notebookDbHelper;

    public NotebookDbAdapter (Context ctx){context=ctx;}

    public NotebookDbAdapter open() throws android.database.SQLException {
        notebookDbHelper=new NotebookDbHelper(context);
        sqlDB=notebookDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){notebookDbHelper.close();}

    public Note createNote(String title, String message){
        Log.d("INSIDE CREATE NOTE","CREATE NOTE");
        ContentValues values=new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);

        long insertId = sqlDB.insert(NOTE_TABLE, null, values);
        Log.d("INSIDE CREATE insertid =>", insertId +"");
        Cursor cursor= sqlDB.query(NOTE_TABLE, allColumns, COLUMN_ID + "=" + insertId, null, null, null, null);

        cursor.moveToFirst();
        Note newNote= cursorToNote(cursor);
        cursor.close();
        return newNote;
    }
    public long deleteNote (long idToDelete){
        return sqlDB.delete(NOTE_TABLE, COLUMN_ID + " = " + idToDelete, null);
    }

    public long updateNote (long idToUpdate, String newTitle, String newMessage){
        ContentValues values=new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_MESSAGE, newMessage);

        return sqlDB.update(NOTE_TABLE, values, COLUMN_ID + "=" +idToUpdate, null);
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes= new ArrayList<Note>();
          Log.d("INSIDE GETALLL","INSIDE GETALLL");
        Cursor cursor=sqlDB.query(NOTE_TABLE, allColumns, null, null, null, null, null);

        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
             Log.d("INSIDE NOTEDB ADP", "INSIDE GET ALL NOTES");
            Note note=cursorToNote(cursor);
            notes.add(note);
        }

        cursor.close();

        return notes;
    }

    private Note cursorToNote(Cursor cursor){
            Log.d("INSIDE Cursor to note","getting vakue");
        Log.d("INSIDE Cursor to note",cursor.getString(2));

        Note newNote=new Note (cursor.getLong(0), cursor.getString(1),
                cursor.getString(2));
        Log.d("INSIDE Cursor to note","PASSED");
        Log.d("INSIDE Cursor to note",cursor.getLong(0)+""+cursor.getString(1)+cursor.getString(2));
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
