package com.example.guessthenumber



import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyDBHelper(context:Context) : SQLiteOpenHelper(context,"ScoreDB",null,1) {




    override fun onCreate(db: SQLiteDatabase?)
    {
        db?.execSQL( "CREATE TABLE USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT, UNAME TEXT, SCORE INTEGER)")







    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int)
    {

    }


}