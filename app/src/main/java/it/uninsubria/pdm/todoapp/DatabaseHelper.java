package it.uninsubria.pdm.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todolist.db";
    // Books table name
    private static final String TABLE_NAME = "todoItems";
    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TASK = "task";
    private static final String KEY_DATE = "creation_date";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create books table
        db.execSQL("create table "
                + TABLE_NAME + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_TASK + " TEXT not null, "
                + KEY_DATE + " INTEGER"
                + ");");
        Log.d(TAG, "onCreate(): create table");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // create fresh books table
        this.onCreate(db);
        Log.d(TAG, "onUpgrade(): created fresh table");
    }

    public long insertItem(TodoItem item){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TASK, item.getTask()); // get todo task
        values.put(KEY_DATE, item.getDate().getTimeInMillis()); // get creation date
        // 3. insert
        long id = db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        //for logging
        Log.d(TAG, "insertItem("+id+") " + item.toString());
        // 4. close
        db.close();
        return id;
    }

    public List<TodoItem> getAllItems() {
        List<TodoItem> items = new LinkedList<TodoItem>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        TodoItem task = null;
        if (cursor.moveToFirst()) {
            do {
                task = new TodoItem();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTask(cursor.getString(1));
                GregorianCalendar createOn = new GregorianCalendar();
                createOn.setTimeInMillis(cursor.getLong(2));
                task.setDate(createOn);
                items.add(task);      // Add item to items
            } while (cursor.moveToNext());
        }
        // 4. close
        db.close();
        Log.d(TAG, "getAllItems(): "+ items.toString());
        return items; // return items
    }

    public void deleteItem(TodoItem item) {

        // 1. get reference to writable DB
        //    Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_NAME, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(item.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d(TAG, "deleted item "+item.toString());

    }
}
