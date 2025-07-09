package com.example.scipy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "User.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table user(userid integer primary key autoincrement,name varchar(20),pass varchar(20),email varchar(20),contact INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS product (productid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, subcatogryid TEXT, price TEXT, img INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cart (cartid INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT, productid TEXT, quantity INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS wishlist (wishlistid INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT, productid TEXT, img INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
