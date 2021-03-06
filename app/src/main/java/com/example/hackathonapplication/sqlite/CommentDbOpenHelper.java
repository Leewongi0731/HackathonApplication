package com.example.hackathonapplication.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CommentDbOpenHelper {
    private String DATABASE_NAME = "Comment.db";
    private int DATABASE_VERSION = 1;
    private SQLiteDatabase sqLiteDatabase;
    private CommentDbOpenHelper.DatabaseHelper databaseHelper;
    private Context context;

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CommentDB.CreateDB._CREATE_COMMENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CommentDB.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public CommentDbOpenHelper(Context context){
        this.context = context;
    }

    public CommentDbOpenHelper open() throws SQLException {
        databaseHelper = new CommentDbOpenHelper.DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        databaseHelper.onCreate(sqLiteDatabase);
    }

    public void close(){
        sqLiteDatabase.close();
    }

    public long insertColumn(String writeremail, String boardkey, String contents, String commentdate, Integer like) {
        ContentValues values = new ContentValues();
        values.put(CommentDB.CreateDB.WRITEREMAIL, writeremail);
        values.put(CommentDB.CreateDB.BOARDKEY, boardkey);
        values.put(CommentDB.CreateDB.CONTENTS, contents);
        values.put(CommentDB.CreateDB.COMMENTDATE, commentdate);
        values.put(CommentDB.CreateDB.LIKE, like);
        return sqLiteDatabase.insert(CommentDB.CreateDB._TABLENAME, null, values);
    }

    public Cursor sortColumn(String sort) {
        Cursor cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + CommentDB.CreateDB._TABLENAME + " ORDER BY " + sort + ";", null);
        return cursor;
    }
    public Cursor sortColumnDesc(String sort) {
        Cursor cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + CommentDB.CreateDB._TABLENAME + " ORDER BY " + sort + " DESC;", null);
        return cursor;
    }

    public Cursor searchColumns(String columnName, String search) {
        Cursor cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + CommentDB.CreateDB._TABLENAME + " WHERE " + columnName + " = " + search + ";", null);
        return cursor;
    }

    public Cursor searchColumnsDesc(String columnName, String search,String sort) {
        Cursor cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + CommentDB.CreateDB._TABLENAME + " WHERE " + columnName + " = " + search + " ORDER BY " + sort + " DESC;", null);
        return cursor;
    }

    public Cursor selectColumns() {
        Cursor cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + CommentDB.CreateDB._TABLENAME + ";", null);
        return cursor;
    }

    public void deleteAllColumns() {
        sqLiteDatabase.delete(CommentDB.CreateDB._TABLENAME, null, null);
    }

    public void deleteColumn(Integer id) {
        sqLiteDatabase.execSQL("DELETE FROM '" + CommentDB.CreateDB._TABLENAME + "' WHERE _id = '" + id + "';");
    }
}
