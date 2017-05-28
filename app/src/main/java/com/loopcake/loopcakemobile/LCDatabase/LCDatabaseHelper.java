package com.loopcake.loopcakemobile.LCDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.loopcake.loopcakemobile.LCExpandableList.Announcement;
import com.loopcake.loopcakemobile.R;

import java.util.ArrayList;

/**
 * Created by Melih on 28.05.2017.
 */

public class LCDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "starbuzz"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    public LCDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "USER_ID TEXT UNIQUE, "
                    + "USERNAME TEXT, "
                    + "EMAIL TEXT, "
                    + "NAME TEXT, "
                    + "SURNAME TEXT, "
                    + "UNIVERSITY TEXT, "
                    + "PHOTO BLOB); "
            );
            db.execSQL("CREATE TABLE USER_REPO (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "USER_ID TEXT, "
                    + "REPO_ID TEXT); "
            );
            db.execSQL("CREATE TABLE REPO (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "REPO_ID TEXT"
                    + "NAME TEXT);"
            );
            db.execSQL("CREATE TABLE BRANCH (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "REPO_ID TEXT"
                    + "NAME TEXT );"
            );
            db.execSQL("CREATE TABLE FILE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "BRANCH_ID INTEGER"
                    + "NAME TEXT, "
                    + "CODE TEXT);"
            );
            db.execSQL("CREATE TABLE COURSE ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "COURSE_ID TEXT UNIQUE,"
                    + "NAME TEXT, "
                    + "CODE TEXT);"
            );
            db.execSQL("CREATE TABLE ANNOUNCEMENT (  _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "ANNOUNCEMENT_ID TEXT UNIQUE, "
                    + "COURSE_ID TEXT, "
                    + "TITLE TEXT, "
                    + "CONTENT TEXT, "
                    + "DATE TEXT);"
                    );
        }
        //insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter);
    }

    private static void insertDrink(SQLiteDatabase db, String name,
                                    String description, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
    }

    public void insertAnnouncement(String id,String course_id,String title,String content, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues announcementValues = new ContentValues();
        announcementValues.put("ANNOUNCEMENT_ID",id);
        announcementValues.put("COURSE_ID",course_id);
        announcementValues.put("TITLE",title);
        announcementValues.put("CONTENT",content);
        db.insertWithOnConflict("ANNOUNCEMENT",null,announcementValues,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Announcement> getUserAnnouncements(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Announcement> announcements=new ArrayList<Announcement>();
        Cursor cursor = db.query("ANNOUNCEMENT",new String[]{"ANNOUNCEMENT_ID","COURSE_ID","TITLE", "CONTENT","DATE"},null,null,null,null,null);
        if (cursor.moveToFirst()) {
            //Get the drink details from the cursor
            while (!cursor.isAfterLast()) {
                Log.d("Found announcement",cursor.getString(0));
                String announcement_id = cursor.getString(0);
                String course_id = cursor.getString(1);
                String title = cursor.getString(2);
                String content = cursor.getString(3);
                String date = cursor.getString(4);
                Announcement temp = new Announcement(announcement_id,title,date,content,course_id);
                announcements.add(temp);
                cursor.moveToNext();
            }
        }
        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return announcements;
    }

}
