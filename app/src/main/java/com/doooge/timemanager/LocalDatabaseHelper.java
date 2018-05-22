package com.doooge.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


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
    private static LocalDatabaseHelper sInstance;

    private LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static synchronized LocalDatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new LocalDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create SpecificTasks_Table
        sqLiteDatabase.execSQL("create table " + SPECIFICTASKS_TABLE_NAME + " (" +
                SPECIFICTASKS_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SPECIFICTASKS_NAME + " TEXT NOT NULL," +
                SPECIFICTASKS_ISCOMPLETED + " INTEGER NOT NULL," +
                SPECIFICTASKS_START_DATE + " TEXT NOT NULL," +
                SPECIFICTASKS_END_DATE + " TEXT NOT NULL," +
                SPECIFICTASKS_TYPE + " INTEGER NOT NULL)");
        //create Tasks_Table
        sqLiteDatabase.execSQL("create table " + TASKS_TABLE_NAME + " (" +
                TASKS_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TASKS_NAME + " TEXT NOT NULL," +
                TASKS_TYPE + " INTEGER NOT NULL)");
        //create Types_Table
        sqLiteDatabase.execSQL("create table " + TYPES_TABLE_NAME + " (" +
                TYPES_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TYPES_NAME + " TEXT NOT NULL," +
                TYPES_COLOR + " TEXT NOT NULL)");
        //Default type
        Type defaultType = new Type("default", "-3155748");//-1 stand for light gray: default color
        defaultType.setId(-999);
        //insertToTypeTable(defaultType);
        ContentValues contentValues = new ContentValues();
        String typeName = defaultType.getName();
        String typeColor = defaultType.getColor();
        int id = defaultType.getId();
        contentValues.put(TYPES_PRIMARY_KEY, id);
        contentValues.put(TYPES_NAME, typeName);
        contentValues.put(TYPES_COLOR, typeColor);
        long result = sqLiteDatabase.insert(TYPES_TABLE_NAME, null, contentValues);
        if (result == -1) {
            throw new AssertionError();
        }
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
    public boolean insertToSpecificTaskTable(SpecificTask specificTask) throws IllegalArgumentException {
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
    public boolean insertToTaskTable(Task task) throws IllegalArgumentException {
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
    public boolean insertToTypeTable(Type type) throws IllegalArgumentException {
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
        updateTaskAndSpecificTaskTableAfterDeletedType(id);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(TYPES_TABLE_NAME, TYPES_PRIMARY_KEY + "=" + id, null);
        return result != 0;
    }

    private void updateTaskAndSpecificTaskTableAfterDeletedType(int typeId) {
//        try {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues specificTaskContentValues = new ContentValues();
        specificTaskContentValues.put(SPECIFICTASKS_TYPE, -999);
        sqLiteDatabase.update(SPECIFICTASKS_TABLE_NAME, specificTaskContentValues, SPECIFICTASKS_TYPE + "=" + typeId, null);
        ContentValues taskContentValues = new ContentValues();
        taskContentValues.put(TASKS_TYPE, -999);
        sqLiteDatabase.update(TASKS_TABLE_NAME, taskContentValues, TASKS_TYPE + "=" + typeId, null);
//        } catch (Exception n){
//            throw new IllegalStateException();
//        }
    }

    //UPDATING METHOD FOR THREE TABLES

    /**
     * This method is used to update a specific Type based on id (Primary Key) user provided.
     * @param specificTask  The new instance of SpecificTask.
     * @return return whether the update is succeed.
     */
    public boolean updateSpecificTaskTable(SpecificTask specificTask) throws IllegalArgumentException {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPECIFICTASKS_NAME, specificTask.getTaskName());
        contentValues.put(SPECIFICTASKS_ISCOMPLETED, specificTask.isCompleted());
        contentValues.put(SPECIFICTASKS_START_DATE, CalendarHelper.convertCal2UTC(specificTask.getStartTime()));
        contentValues.put(SPECIFICTASKS_END_DATE, CalendarHelper.convertCal2UTC(specificTask.getEndTime()));
        contentValues.put(SPECIFICTASKS_TYPE, specificTask.getType().getId());
        int id = specificTask.getId();
        long result = sqLiteDatabase.update(SPECIFICTASKS_TABLE_NAME, contentValues, SPECIFICTASKS_PRIMARY_KEY + "=" + id, null);
        return result != 0;//Ensure success: Affect more than 0 rows
    }

    /**
     * This method is used to update a specific Task based on id (Primary Key) user provided.
     * @param task The new instance of Task
     * @return return whether the insertion is succeed.
     */
    public boolean updateTaskTable(Task task) throws IllegalArgumentException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_NAME, task.getTaskName());
        contentValues.put(TASKS_TYPE, task.getType().getId());
        int id = task.getId();
        long result = sqLiteDatabase.update(TASKS_TABLE_NAME, contentValues, TASKS_PRIMARY_KEY + "=" + id, null);
        return (result != 0);//Ensure success: Affect more than 0 rows
    }

    /**
     * This method is used to update a specific Type based on id (Primary Key) user provided.
     * @param type The new instance of Type
     * @return return whether the insertion is succeed.
     */
    public boolean updateTypeTable(Type type) throws IllegalArgumentException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPES_NAME, type.getName());
        contentValues.put(TYPES_COLOR, type.getColor());
        int id = type.getId();
        long result = sqLiteDatabase.update(TYPES_TABLE_NAME, contentValues, TYPES_PRIMARY_KEY + "=" + id, null);
        return result != 0;//Ensure success: Affect more than 0 rows
    }

    //HELPER METHODS
    /**
     * 【***】ALWAYS: ADD AN "TO_DO" BEFORE USE IT AND SET TO PRIVATE AFTER DEBUGGED!【***】
     *  To use it: set it as public.
     *  This method is used for displaying current Database for debugging only.
     *
     * @param context pass current context
     */
    private void showAllData(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder buffer = new StringBuilder();

        Cursor cursor1 = db.query(SPECIFICTASKS_TABLE_NAME, null, null, null, null, null, null, null);
        buffer.append("\n\n\n【***】PLEASE GO TO LDH TO SET THIS METHOD AS PRIVATE AFTER EACH USE!!!【***】\n\n\n");
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
    public Type findTypeByPrimaryKey(int key) throws IllegalArgumentException {
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
            Task Task = new Task(name);
            Task.setId(id);
            Type type = findTypeByPrimaryKey(typeID);
            Task.setType(type);
            Tasks.add(Task);
        }

        return Tasks;
    }

    /**
     * This method returns an arrayList of Type objects with given cursor
     *
     * @param cursor given cursor
     * @return Type Object
     */
    private ArrayList<Type> findTypeByCursor(Cursor cursor) {
        ArrayList<Type> Types = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(0));
            String name = cursor.getString(1);
            String typeColor = cursor.getString(2);
            Type type = new Type(name, typeColor);
            type.setId(id);
            Types.add(type);
        }

        return Types;
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
    public ArrayList<SpecificTask> findSpecificTasksByTime(Calendar day) {
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
    public ArrayList<SpecificTask> findSpecificTasksByTypes(Type type) throws IllegalArgumentException {
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
    public ArrayList<Task> findTasksByType(Type type) throws IllegalArgumentException {
        SQLiteDatabase db = this.getWritableDatabase();
        String typeID = String.valueOf(type.getId());
        String selection = TASKS_TYPE + " =? ";
        Cursor cursor = db.query(TASKS_TABLE_NAME, null, selection, new String[]{typeID}, null, null, TASKS_NAME + " COLLATE NOCASE");
        return findTaskByCursor(cursor);
    }

    /**
     * get all the specificTasks stored in dB
     *
     * @return Arraylist of SpecificTask Objects
     */
    public ArrayList<SpecificTask> getAllSpecificTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SPECIFICTASKS_TABLE_NAME, null, null, null, null, null, SPECIFICTASKS_START_DATE + " ASC");
        return findSpecificTaskByCursor(cursor);
    }

    /**
     * get all the tasks stored in dB
     * @return Arraylist of Task Objects
     */
    public ArrayList<Task> getAllTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TASKS_TABLE_NAME, null, null, null, null, null, null);
        return findTaskByCursor(cursor);
    }

    /**
     * get all the types stored in dB
     * @return Arraylist of Type Objects
     */
    public ArrayList<Type> getAllType() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TYPES_TABLE_NAME, null, null, null, null, null, null);
        return findTypeByCursor(cursor);
    }


    /**
     * get a collection of SpecificTasks that belong to the given collection of types
     *
     * @param types The specificTasks' types you are looking for
     * @return ArrayList of SpecificTasks that belong to the given collection of types.
     */
    public ArrayList<SpecificTask> findSpecificTasksByTypes(ArrayList<Type> types) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] typeID = new String[types.size()];
        String selection = SPECIFICTASKS_TYPE + " =?";
        Iterator<Type> iterator = types.iterator();
        if (types.size() == 0) {
            throw new IndexOutOfBoundsException();//Always have at least one type
        }
        typeID[0] = iterator.next().getId() + "";
        int i = 1;
        while (iterator.hasNext()) {
            String singleTypeID = iterator.next().getId() + "";
            typeID[i] = singleTypeID;
            selection = selection + "OR " + SPECIFICTASKS_TYPE + " =?";
            i++;
        }
        Cursor cursor = db.query(SPECIFICTASKS_TABLE_NAME, null, selection, typeID, null, null, SPECIFICTASKS_START_DATE + " ASC");
        return findSpecificTaskByCursor(cursor);
    }

    /**
     * get a collection of SpecificTasks that start and end in the given period of time (start day and end day are all INCLUSIVE)
     *
     * @param start The start time (INCLUSIVE) that the collection of SpecificTasks you are looking for
     * @param end   The end time (INCLUSIVE) that the collection of SpecificTakss you are looking for
     * @return ArrayList of SpecificTask Objects that start and end in the given period of time
     * @exception IllegalStateException: start day must be the same as or before the end day!
     */
    public ArrayList<SpecificTask> findSpecificTasksByTime(Calendar start, Calendar end) {

        String startCalendarInString = CalendarHelper.convertCal2UTC(start);
        String startCalendarInDay = startCalendarInString.substring(0, 10);//build up a subString in the form of yyyy-mm-dd. Example: 1993-08-21.

        String endCalendarInString = CalendarHelper.convertCal2UTC(end);
        String endCalendarInDay = endCalendarInString.substring(0, 10);//build up a subString in the form of yyyy-mm-dd. Example: 1993-08-21.

        if (startCalendarInDay.compareTo(endCalendarInDay) > 0) {
            throw new IllegalStateException();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "strftime('%Y-%m-%d'," + SPECIFICTASKS_START_DATE + ") BETWEEN ? AND ? ";
        String[] dayCondition = new String[2];
        dayCondition[0] = startCalendarInDay;
        dayCondition[1] = endCalendarInDay;
        Cursor cursor = db.query(SPECIFICTASKS_TABLE_NAME, null, selection, dayCondition, null, null, SPECIFICTASKS_START_DATE + " ASC");
        return findSpecificTaskByCursor(cursor);
    }


    public ArrayList<SpecificTask> findSpecificTasksByTypesDuringTime(ArrayList<Type> types, Calendar start, Calendar end) {

        SQLiteDatabase db = this.getWritableDatabase();

        String[] typeID = new String[types.size() + 2];

        //selections and selection arguments for days:

        String startCalendarInString = CalendarHelper.convertCal2UTC(start);
        String startCalendarInDay = startCalendarInString.substring(0, 10);//build up a subString in the form of yyyy-mm-dd. Example: 1993-08-21.

        String endCalendarInString = CalendarHelper.convertCal2UTC(end);
        String endCalendarInDay = endCalendarInString.substring(0, 10);//build up a subString in the form of yyyy-mm-dd. Example: 1993-08-21.

        if (startCalendarInDay.compareTo(endCalendarInDay) > 0) {
            throw new IllegalStateException();
        }

        String selection = "( strftime('%Y-%m-%d'," + SPECIFICTASKS_START_DATE + ") BETWEEN ? AND ? ) AND ( " + SPECIFICTASKS_TYPE + " =?";
        typeID[0] = startCalendarInDay;
        typeID[1] = endCalendarInDay;
        Iterator<Type> iterator = types.iterator();

        if (types.size() == 0) {
            throw new IndexOutOfBoundsException();//Always have at least one type
        }

        //selections and selection arguments for types:
        typeID[2] = iterator.next().getId() + "";
        int i = 3;
        while (iterator.hasNext()) {
            String singleTypeID = iterator.next().getId() + "";
            typeID[i] = singleTypeID;
            selection = selection + "OR " + SPECIFICTASKS_TYPE + " =?";
            i++;
        }

        selection = selection + " )";

        Cursor cursor = db.query(SPECIFICTASKS_TABLE_NAME, null, selection, typeID, null, null, SPECIFICTASKS_START_DATE + " ASC");
        return findSpecificTaskByCursor(cursor);
    }

}
