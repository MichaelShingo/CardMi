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
    public static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "flashcard.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + FLASHCARD_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FLASHCARD_SET + " TEXT)";
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

    public ArrayList<Integer> getIDs(){
        ArrayList<Integer> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + FLASHCARD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase(); //this way database is not locked, as opposed to writable
        Cursor cursor = db.rawQuery(queryString, null); //cursor is the result set

        if (cursor.moveToFirst()) {
            //loop through cursor result set and put objects into returnList
            do {
               // int id = cursor.getInt(0);
                Integer id = cursor.getInt(0);
                returnList.add(id);

            } while (cursor.moveToNext());
        }
        else{
            //do not add anything
        }

        cursor.close();
        db.close();
        System.out.println(returnList);
        return returnList;

    }

    public void update(int id, String encodedFlashcardSet){
        String idStr = Integer.toString(id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FLASHCARD_SET, encodedFlashcardSet);
        //compare the "ID" column with the int we have from the parameters, converted to string....
        db.update(FLASHCARD_TABLE, cv, "ID=?", new String[]{idStr}); //here the problem....
        db.close();
    }

    public void delete(int id){

        //this is working correctly...but the listview is not updating afterwards
        //and the correct number isn't getting passed to the function???
        System.out.println("Deleting: " + id);
        String idStr = Integer.toString(id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete(FLASHCARD_TABLE, "ID=?", new String[]{idStr});
        db.close();
    }

}
