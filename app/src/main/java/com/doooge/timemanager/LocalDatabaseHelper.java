package com.doooge.timemanager;

import android.annotation.SuppressLint;
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
    public static final String SPECIFICTASKS_TABLE_NAME = "SpecificTasks_Table";
    public static final String TASKS_TABLE_NAME = "Tasks_Table";
    public static final String TYPES_TABLE_NAME = "Types_Table";

    public static final String SPECIFICTASKS_PRIMARY_KEY = "SpecificTasks_ID";
    public static final String SPECIFICTASKS_NAME = "SpecificTasks_name";
    public static final String SPECIFICTASKS_ISCOMPLETED = "SpecificTasks_isCompleted";
    public static final String SPECIFICTASKS_START_DATE = "SpecificTasks_startDate";
    public static final String SPECIFICTASKS_END_DATE = "SpecificTasks_endDate";
    public static final String SPECIFICTASKS_TYPE = "SpecificTasks_type";

    public static final String TASKS_PRIMARY_KEY = "Tasks_ID";
    public static final String TASKS_NAME = "Tasks_name";
    public static final String TASKS_TYPE = "Tasks_type";

    public static final String TYPES_PRIMARY_KEY = "Type_ID";
    public static final String TYPES_NAME = "Type_name";
    public static final String TYPES_COLOR = "Type_color";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create SpecificTasks_Table
        sqLiteDatabase.execSQL("create table " + SPECIFICTASKS_TABLE_NAME + " (" +
                SPECIFICTASKS_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SPECIFICTASKS_NAME + " TEXT," +
                SPECIFICTASKS_ISCOMPLETED + " INTEGER," +
                SPECIFICTASKS_START_DATE + " TEXT," +
                SPECIFICTASKS_END_DATE + " TEXT," +
                SPECIFICTASKS_TYPE + " INTEGER)");
        //create Tasks_Table
        sqLiteDatabase.execSQL("create table " + TASKS_TABLE_NAME + " (" +
                TASKS_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TASKS_NAME + " TEXT," +
                TASKS_TYPE + " INTEGER)");
        //create Types_Table
        sqLiteDatabase.execSQL("create table " + TYPES_TABLE_NAME + " (" +
                TYPES_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TYPES_NAME + " TEXT," +
                TYPES_COLOR + " TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SpecificTasks_Table");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Tasks_Table");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Types_Table");
        onCreate(sqLiteDatabase);
    }

    //INSERTION FOR THREE TABLES

    /**
     * @param specificTaskName        add specificTask's name
     * @param specificTaskIsCompleted add specificTask's complement condition
     * @param specificTaskStartDate   add specificTask's start date in UTC format
     * @param specificTaskEndDate     add specificTask's end date in UTC format
     * @param specificTaskType        add specificTask's type
     * @return return whether the insertion is succeed.
     */
    public boolean insertToSpecificTaskTable(String specificTaskName, int specificTaskIsCompleted, String specificTaskStartDate, String specificTaskEndDate, int specificTaskType) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPECIFICTASKS_NAME, specificTaskName);
        contentValues.put(SPECIFICTASKS_ISCOMPLETED, specificTaskIsCompleted);
        contentValues.put(SPECIFICTASKS_START_DATE, specificTaskStartDate);
        contentValues.put(SPECIFICTASKS_END_DATE, specificTaskEndDate);
        contentValues.put(SPECIFICTASKS_TYPE, specificTaskType);
        long result = sqLiteDatabase.insert("SpecificTasks_Table", null, contentValues);
        return (result != -1);
    }

    /**
     * @param taskName add Task's name
     * @param taskType add Task's type
     * @return return whether the insertion is succeed.
     */
    public boolean insertToTaskTable(String taskName, int taskType) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_NAME, taskName);
        contentValues.put(TASKS_TYPE, taskType);
        long result = sqLiteDatabase.insert("Tasks_Table", null, contentValues);
        return (result != -1);
    }

    /**
     * @param typeName add Type's name
     * @param typeColor add Type's color
     * @return return whether the insertion is succeed.
     */
    public boolean insertToTypeTable(String typeName, String typeColor) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPES_NAME, typeName);
        contentValues.put(TYPES_COLOR, typeColor);
        long result = sqLiteDatabase.insert("Types_Table", null, contentValues);
        return (result != -1);
    }

    //DELETION FOR THREE TABLES
    //TODO DELETION FOR THREE TABLES
    //UPDATING METHOD FOR THREE TABLES
    //TODO UPDATING METHOD FOR THREE TABLES

    /**
     * @param context pass current context
     *                <p>
     *                This method is used for displaying current Database for debugging only.
     */
    public void showAllData(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder buffer = new StringBuilder();

        @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery("select * from " + SPECIFICTASKS_TABLE_NAME, null);
        buffer.append("===SpecificTasks_TABLE:===").append("\n");
        while (cursor1.moveToNext()) {
            buffer.append("SpecificTasks_ID: ").append(cursor1.getString(0)).append("\n");
            buffer.append("SpecificTasks_name: ").append(cursor1.getString(1)).append("\n");
            buffer.append("SpecificTasks_isCompleted: ").append(cursor1.getString(2)).append("\n");
            buffer.append("SpecificTasks_startDate: ").append(cursor1.getString(3)).append("\n");
            buffer.append("SpecificTasks_endDate: ").append(cursor1.getString(4)).append("\n");
            buffer.append("SpecificTasks_type: ").append(cursor1.getString(5)).append("\n\n");
        }
        @SuppressLint("Recycle") Cursor cursor2 = db.rawQuery("select * from " + TASKS_TABLE_NAME, null);
        buffer.append("===Tasks_TABLE:===").append("\n");
        while (cursor2.moveToNext()) {
            System.out.println("=================================================");
            buffer.append("Tasks_ID: ").append(cursor2.getString(0)).append("\n");
            buffer.append("Tasks_name: ").append(cursor2.getString(1)).append("\n");
            StringBuilder append = buffer.append("Tasks_type: ").append(cursor2.getString(2)).append("\n\n");

        }
        @SuppressLint("Recycle") Cursor cursor3 = db.rawQuery("select * from " + TYPES_TABLE_NAME, null);
        buffer.append("===Types_TABLE:===").append("\n");
        while (cursor3.moveToNext()) {
            buffer.append("Types_ID: ").append(cursor3.getString(0)).append("\n");
            buffer.append("Types_name: ").append(cursor3.getString(1)).append("\n");
            buffer.append("Types_color: ").append(cursor3.getString(2)).append("\n\n");

        }

        showInAlert("Testing Database Tables: ", buffer.toString(), context);
    }

    /**
     * @param title Dialog title
     * @param message message to be displayed
     * @param context the context to appear
     *
     */
    private void showInAlert(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
