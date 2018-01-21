package com.doooge.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;


/**
 * Created by fredpan on 2018/1/20.
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TIMEMANAGER.db";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table MyDb (Fred INTEGER PRIMARY KEY AUTOINCREMENT, Vsion TEXT, Diana TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MyDb");
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String col1, String col2) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Vsion", col1);
        contentValues.put("Diana", col2);
        long result = sqLiteDatabase.insert("MyDb", null, contentValues);
        return (result != -1);
    }

    public void showAllData(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from MyDb", null);
        if (cursor.getCount() == 0) {
            showInAlert("Error:", "No data found!", context);
        }
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append("Fred: " + cursor.getString(0) + "\n");
            buffer.append("Vsion: " + cursor.getString(1) + "\n");
            buffer.append("Diana: " + cursor.getString(2) + "\n\n");
        }

        showInAlert("Table: MyDb", buffer.toString(), context);
    }

    private void showInAlert(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
