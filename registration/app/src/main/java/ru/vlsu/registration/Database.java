package ru.vlsu.registration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public static final String DBNAME = "AppDatabase.db";

    public Database(@Nullable Context context) {
        super(context, "AppDatabase.db", null, 1);
    }

    @Override
    public void onCreate (SQLiteDatabase myDb){
        myDb.execSQL("create Table users(user TEXT primary key, password TEXT, name TEXT)");
    }

    @Override
    public void onUpgrade (SQLiteDatabase myDb, int prevVersion, int currentVersion){
        myDb.execSQL("drop table if exists users");

        onCreate(myDb);
    }

    public Boolean insertData(String user, String name, String password){
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("name", name);
        contentValues.put("password", password);
        Long results = myDb.insert("users", null, contentValues);
        if(results.equals(-1))
            return false;
        else
            return true;
    }

    public Boolean checkUser(String email){
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from users where user = ?", new String[] {email});
        if(cursor.getCount()>0){
            return true;
        }
        else
            return false;
    }

    public Boolean checkUserPassword(String user, String password){
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from users where user = ? and password = ?", new String[] {user, password});
        if(cursor.getCount()>0){
            return true;
        }
        else
            return false;
    }

}
