package com.doooge.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by fredpan on 2018/1/20.
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TIMEMANAGER.db";
    private static final String SPECIFICTASKS_TABLE_NAME = "SpecificTasks_Table";
    private static final String TASKS_TABLE_NAME = "Tasks_Table";
    private static final String TYPES_TABLE_NAME = "Types_Table";

    private static final String SPECIFICTASKS_PRIMARY_KEY = "SpecificTask_ID";
    private static final String SPECIFICTASKS_NAME = "SpecificTask_name";
    private static final String SPECIFICTASKS_ISCOMPLETED = "SpecificTask_isCompleted";
    private static final String SPECIFICTASKS_START_DATE = "SpecificTask_startDate";
    private static final String SPECIFICTASKS_END_DATE = "SpecificTask_endDate";
    private static final String SPECIFICTASKS_TYPE = "SpecificTask_type";

    private static final String TASKS_PRIMARY_KEY = "Tasks_ID";
    private static final String TASKS_NAME = "Task_name";
    private static final String TASKS_TYPE = "Task_type";

    private static final String TYPES_PRIMARY_KEY = "Type_ID";
    private static final String TYPES_NAME = "Type_name";
    private static final String TYPES_COLOR = "Type_color";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
     * This method is used for insert a new specificTask into the table SpecificTasks_Table in Local SQLite Database.
     * @param specificTask the specificTask to be added
     * @return return whether the insertion is succeed.
     *
     */
    public boolean insertToSpecificTaskTable(SpecificTask specificTask) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPECIFICTASKS_NAME, specificTask.getTaskName());
        contentValues.put(SPECIFICTASKS_ISCOMPLETED, specificTask.isCompleted());
        contentValues.put(SPECIFICTASKS_START_DATE, CalendarHelper.convertCal2UTC(specificTask.getStartTime()));
        contentValues.put(SPECIFICTASKS_END_DATE, CalendarHelper.convertCal2UTC(specificTask.getEndTime()));
        contentValues.put(SPECIFICTASKS_TYPE, specificTask.getType().getId());
        long result = sqLiteDatabase.insert(SPECIFICTASKS_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    /**
     * This method is used for insert a new Task into the table Tasks_Table in Local SQLite Database.
     * @param task the task to be added
     * @return return whether the insertion is succeed.
     */
    public boolean insertToTaskTable(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String taskName = task.getTaskName();
        int taskType = task.getType().getId();
        contentValues.put(TASKS_NAME, taskName);
        contentValues.put(TASKS_TYPE, taskType);
        long result = sqLiteDatabase.insert(TASKS_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    /**
     * This method is used for insert a new specificTask into the table Types_Table in Local SQLite Database.
     * @param type the type to be added
     * @return return whether the insertion is succeed.
     */
    public boolean insertToTypeTable(Type type) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String typeName = type.getName();
        String typeColor = type.getColor();
        contentValues.put(TYPES_NAME, typeName);
        contentValues.put(TYPES_COLOR, typeColor);
        long result = sqLiteDatabase.insert(TYPES_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    //DELETION FOR THREE TABLES

    /**
     * This method is used to delete a specific SpecificTask based on id (Primary Key) user provided.
     *
     * @param id input the id of the specificTask needs to be deleted
     * @return return whether the deletion is successed
     */
    public boolean deleteSpecificTaskTable(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(SPECIFICTASKS_TABLE_NAME, SPECIFICTASKS_PRIMARY_KEY + "=" + id, null);
        return result != 0;
    }

    /**
     * This method is used to delete a specific Task based on id (Primary Key) user provided.
     *
     * @param id input the id of the Task needs to be deleted
     * @return return whether the deletion is successed
     */
    public boolean deleteTaskTable(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(TASKS_TABLE_NAME, TASKS_PRIMARY_KEY + "=" + id, null);
        return result != 0;
    }

    /**
     * This method is used to delete a specific Type based on id (Primary Key) user provided.
     *
     * @param id input the id of the type needs to be deleted
     * @return return whether the deletion is successed
     */
    public boolean deleteTypeTable(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(TYPES_TABLE_NAME, TYPES_PRIMARY_KEY + "=" + id, null);
        return result != 0;
    }

    //UPDATING METHOD FOR THREE TABLES

    /**
     * This method is used to update a specific Type based on id (Primary Key) user provided.
     *
     * @param id                      input the id of the specificTask needs to be updated
     * @param specificTask  The new instance of SpecificTask
     * @return return whether the update is succeed.
     */
    public boolean updateSpecificTaskTable(int id, SpecificTask specificTask) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPECIFICTASKS_NAME, specificTask.getTaskName());
        contentValues.put(SPECIFICTASKS_ISCOMPLETED, specificTask.isCompleted());
        contentValues.put(SPECIFICTASKS_START_DATE, CalendarHelper.convertCal2UTC(specificTask.getStartTime()));
        contentValues.put(SPECIFICTASKS_END_DATE, CalendarHelper.convertCal2UTC(specificTask.getEndTime()));
        contentValues.put(SPECIFICTASKS_TYPE, specificTask.getType().getId());
        long result = sqLiteDatabase.update(SPECIFICTASKS_TABLE_NAME, contentValues, SPECIFICTASKS_PRIMARY_KEY + "=" + id, null);
        System.out.println(result);
        return result != 0;//Ensure success: Affect more than 0 rows
    }

    /**
     * This method is used to update a specific Task based on id (Primary Key) user provided.
     *
     * @param id       input the id of the Task needs to be updated
     * @param task The new instance of Task
     * @return return whether the insertion is succeed.
     */
    public boolean updateTaskTable(int id, Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_NAME, task.getTaskName());
        contentValues.put(TASKS_TYPE, task.getType().getId());
        long result = sqLiteDatabase.update(TASKS_TABLE_NAME, contentValues, TASKS_PRIMARY_KEY + "=" + id, null);
        return (result != 0);//Ensure success: Affect more than 0 rows
    }

    /**
     * This method is used to update a specific Type based on id (Primary Key) user provided.
     *
     * @param id        input the id of the Type needs to be updated
     * @param type The new instance of Type
     * @return return whether the insertion is succeed.
     */
    public boolean updateTypeTable(int id, Type type) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPES_NAME, type.getName());
        contentValues.put(TYPES_COLOR, type.getColor());
        long result = sqLiteDatabase.update(TYPES_TABLE_NAME, contentValues, TYPES_PRIMARY_KEY + "=" + id, null);
        return result != 0;//Ensure success: Affect more than 0 rows
    }

    //HELPER METHODS
    /**
     * This method is used for displaying current Database for debugging only.
     * @param context pass current context
     */
    protected void showAllData(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder buffer = new StringBuilder();

        Cursor cursor1 = db.query(SPECIFICTASKS_TABLE_NAME, null, null, null, null, null, null, null);
        buffer.append("===SpecificTasks_TABLE:===").append("\n");
        while (cursor1.moveToNext()) {
            buffer.append("SpecificTasks_ID: ").append(cursor1.getString(0)).append("\n");
            buffer.append("SpecificTasks_name: ").append(cursor1.getString(1)).append("\n");
            buffer.append("SpecificTasks_isCompleted: ").append(cursor1.getString(2)).append("\n");
            buffer.append("SpecificTasks_startDate: ").append(cursor1.getString(3)).append("\n");
            buffer.append("SpecificTasks_endDate: ").append(cursor1.getString(4)).append("\n");
            buffer.append("SpecificTasks_type: ").append(cursor1.getString(5)).append("\n\n");
        }
        Cursor cursor2 = db.query(TASKS_TABLE_NAME, null, null, null, null, null, null, null);
        buffer.append("===Tasks_TABLE:===").append("\n");
        while (cursor2.moveToNext()) {
            buffer.append("Tasks_ID: ").append(cursor2.getString(0)).append("\n");
            buffer.append("Tasks_name: ").append(cursor2.getString(1)).append("\n");
            StringBuilder append = buffer.append("Tasks_type: ").append(cursor2.getString(2)).append("\n\n");

        }
        Cursor cursor3 = db.query(TYPES_TABLE_NAME, null, null, null, null, null, null, null);
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

    // FIND TYPE BY PRIMARY KEY.

    /**
     * This method returns Type object with given Primary Key
     *
     * @return Type Object
     * @key Primary key of TypeTable
     */
    private Type findTypeByPrimaryKey(int key) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id;
        String name;
        String color;
        String sqlReq = TYPES_PRIMARY_KEY + " =?";
        Cursor cursor = db.query(TYPES_TABLE_NAME, null, sqlReq, new String[]{key + ""}, null, null, null, null);
        cursor.moveToNext();
        id = Integer.parseInt(cursor.getString(0));
        name = cursor.getString(1);
        color = cursor.getString(2);
        Type type = new Type(name, color);
        type.setId(id);
        return type;
    }

    // FIND OBJECT(SPECIFICTASKS && TASKS) BY CURSOR

    /**
     * This method returns an arrayList of SpecificTask objects with given cursor
     * @param cursor given cursor
     * @return SpecificTask Object
     */
    private ArrayList<SpecificTask> findSpecificTaskByCursor(Cursor cursor) {
        ArrayList<SpecificTask> specificTasks = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(0));
            String name = cursor.getString(1);
            Integer isCompleted = Integer.parseInt(cursor.getString(2));
            Calendar startDate = CalendarHelper.convertUTC2Cal(cursor.getString(3));
            Calendar endDate = CalendarHelper.convertUTC2Cal(cursor.getString(4));
            Integer typeID = Integer.parseInt(cursor.getString(5));
            SpecificTask specificTask = new SpecificTask(name, startDate, endDate);
            specificTask.setCompleted(isCompleted);
            specificTask.setId(id);
            specificTask.setStartTime(startDate);
            Type type = findTypeByPrimaryKey(typeID);
            specificTask.setType(type);
            specificTasks.add(specificTask);
        }

        return specificTasks;
    }

    /**
     * This method returns an arrayList of Task objects with given cursor
     *
     * @param cursor given cursor
     * @return Task Object
     */
    private ArrayList<Task> findTaskByCursor(Cursor cursor) {
        ArrayList<Task> Tasks = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(0));
            String name = cursor.getString(1);
            Integer typeID = Integer.parseInt(cursor.getString(2));
            System.out.println("DB!!!+++++=====" + typeID);
            Task Task = new Task(name);
            Task.setId(id);
            Type type = findTypeByPrimaryKey(typeID);
            Task.setType(type);
            Tasks.add(Task);
        }

        return Tasks;
    }

    /**
     * This method returns SpecificTask object with given Primary Key
     *
     * @return SpecificTask Object
     * @key Primary key of SpecificTaskTable
     */
    private Task findTaskByPrimaryKey(int key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlReq = TASKS_PRIMARY_KEY + " =?";
        Cursor cursor = db.query(TASKS_TABLE_NAME, null, sqlReq, new String[]{key + ""}, null, null, null, null);
        cursor.moveToNext();
        int id;
        String name;
        Integer typeID;
        Task task;
        id = Integer.parseInt(cursor.getString(0));
        name = cursor.getString(1);
        task = new Task(name);
        task.setId(id);
        typeID = Integer.parseInt(cursor.getString(2));
        Type type = findTypeByPrimaryKey(typeID);
        task.setType(type);
        return task;
    }

    // FIND SPECIFICTASKS BY STARTTIME

    /**
     * Given a specific day in Calendar type, this method will return an arrayList that contains all the SpecificTasks that start from that day and are sorted by start-time in increasing time order.
     *
     * @param day The day of SpecificTasks that you want to get in Calendar type.
     * @return An sorted ArrayList of SpecificTasks that start from that day. All the SpecificTasks object are in sort with start-time timing order.
     */
    public ArrayList<SpecificTask> specificTasksSortByStartTime(Calendar day) {
        String calendarInString = CalendarHelper.convertCal2UTC(day);
        String calendarInDay = calendarInString.substring(0, 10);//build up a subString in the form of yyyy-mm-dd. Example: 1993-08-21.
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = SPECIFICTASKS_START_DATE + " LIKE ?";
        Cursor cursor = db.query(SPECIFICTASKS_TABLE_NAME, null, selection, new String[]{"%" + calendarInDay + "%"}, null, null, SPECIFICTASKS_START_DATE + " ASC");
        return findSpecificTaskByCursor(cursor);
    }

    // FIND SPECIFICTASKS && TASKS BY TYPE

    /**
     * Given a specific type, this method will return an arrayList that contains all the SpecificTasks that belongs to the given type and are sorted by start-time in increasing time order.
     *
     * @param type The type of SpecificTasks that you want to get
     * @return An sorted ArrayList of SpecificTasks that belongs to the given type. All the SpecificTasks object are in sort with start-time timing order.
     */
    public ArrayList<SpecificTask> specificTasksByType(Type type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String typeID = String.valueOf(type.getId());
        String selection = SPECIFICTASKS_TYPE + " =? ";
        Cursor cursor = db.query(SPECIFICTASKS_TABLE_NAME, null, selection, new String[]{typeID}, null, null, SPECIFICTASKS_START_DATE + " ASC");
        return findSpecificTaskByCursor(cursor);
    }

    /**
     * Given a specific type, this method will return an arrayList that contains all the Tasks that belongs to the given type and are sorted by non-case sensitive alphabetical order.
     *
     * @param type The type of SpecificTasks that you want to get
     * @return An sorted ArrayList of Tasks that belongs to the given type. All the Tasks object are in sort with non-case sensitive alphabetical order.
     */
    public ArrayList<Task> tasksByType(Type type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String typeID = String.valueOf(type.getId());
        String selection = TASKS_TYPE + " =? ";
        Cursor cursor = db.query(TASKS_TABLE_NAME, null, selection, new String[]{typeID}, null, null, TASKS_NAME + " COLLATE NOCASE");
        return findTaskByCursor(cursor);
    }


    //TODO TESTING, TOBE DELETED
    public void execute(String givenCondition, Context context) {

        HashMap<Integer, SpecificTask> specificTaskHashMap = new HashMap<>();

        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder buffer = new StringBuilder();
        String selection = SPECIFICTASKS_START_DATE + " LIKE ?";
        Cursor cursor1 = db.query(SPECIFICTASKS_TABLE_NAME, null, selection, new String[]{"%" + givenCondition + "%"}, null, null, SPECIFICTASKS_START_DATE + " ASC");
        while (cursor1.moveToNext()) {
            buffer.append("SpecificTasks_ID: ").append(cursor1.getString(0)).append("\n");
            buffer.append("SpecificTasks_name: ").append(cursor1.getString(1)).append("\n");
            buffer.append("SpecificTasks_isCompleted: ").append(cursor1.getString(2)).append("\n");
            buffer.append("SpecificTasks_startDate: ").append(cursor1.getString(3)).append("\n");
            buffer.append("SpecificTasks_endDate: ").append(cursor1.getString(4)).append("\n");
            buffer.append("SpecificTasks_type: ").append(cursor1.getString(5)).append("\n\n");

            showInAlert("Testing Database Tables: ", buffer.toString(), context);

        }
    }
}
