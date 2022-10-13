package com.michaelshingo.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String FLASHCARD_TABLE = "FLASHCARD_TABLE";
    public static final String COLUMN_FLASHCARD_SET = "FLASHCARD_SET";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "flashcard.db", null, 1);
    }





    //for first time you try to access the database, must create new database here

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + FLASHCARD_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FLASHCARD_SET + " TEXT)";
        db.execSQL(createTableStatement);

    }

    //this is called if the database version number changes, for old users, It prevents previous users apps from breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(String encodedFlashcardSet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //associative array, pairs of values
        // you don't need ID because it's auto-increment
        cv.put(COLUMN_FLASHCARD_SET, encodedFlashcardSet);

        long insert = db.insert(FLASHCARD_TABLE, null, cv);//null column hack has to do with inserting empty rows...which you can't have in SQL
        if (insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<String> getAll(){
        ArrayList<String> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + FLASHCARD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase(); //this way database is not locked, as opposed to writable
        Cursor cursor = db.rawQuery(queryString, null); //cursor is the result set

        if (cursor.moveToFirst()) {
            //loop through cursor result set and put objects into returnList
            do {
                int id = cursor.getInt(0);
                String encodedFlashcardSet = cursor.getString(1);
                returnList.add(encodedFlashcardSet);
                System.out.println(returnList);
            } while (cursor.moveToNext());
        }
        else{
            //do not add anything

        }

        cursor.close();
        db.close();
        return returnList;

    }
}
