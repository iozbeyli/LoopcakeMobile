package com.loopcake.loopcakemobile.LCDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.loopcake.loopcakemobile.LCExpandableList.Announcement;
import com.loopcake.loopcakemobile.LCList.LCListItems.Repo;
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
                    + "REPO_ID TEXT, "
                    + "NAME TEXT, "
                    + "CURRENT_BRANCH TEXT, "
                    + "CURRENT_SHA TEXT, "
                    + "UNIQUE(REPO_ID)"
                    + ");"
            );
            db.execSQL("CREATE TABLE BRANCH (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "REPO_ID TEXT"
                    + "NAME TEXT );"
            );
            db.execSQL("CREATE TABLE FILE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "REPO_ID TEXT, "
                    + "BRANCH_NAME TEXT, "
                    + "NAME TEXT, "
                    + "CODE TEXT, "
                    + "UNIQUE(REPO_ID, BRANCH_NAME)"
                    + ");"
            );
            db.execSQL("CREATE TABLE COURSE ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "COURSE_ID TEXT"
                    + "NAME TEXT, "
                    + "CODE TEXT, "
                    + "UNIQUE(COURSE_ID) "
                    + ");"
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

    public void insertAnnouncement(Announcement announcement){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ANNOUNCEMENT_ID",announcement.announcementID);
        values.put("COURSE_ID",announcement.course_id);
        values.put("TITLE",announcement.title);
        values.put("CONTENT",announcement.details);
        values.put("DATE",announcement.date);
        db.insertWithOnConflict("ANNOUNCEMENT",null,values,SQLiteDatabase.CONFLICT_REPLACE);
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

    public void insertRepo(Repo repo){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("REPO_ID",repo.repoID);
        values.put("NAME",repo.repoName);
        if(repo.currentBranch!=null){
            values.put("CURRENT_BRANCH",repo.currentBranch);
            values.put("CURRENT_SHA",repo.currentSha);
        }
        db.insertWithOnConflict("REPO",null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Repo> getRepos(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Repo> repos=new ArrayList<>();
        Cursor cursor = db.query("REPO",new String[]{"REPO_ID","NAME","CURRENT_BRANCH", "CURRENT_SHA"},null,null,null,null,null);
        if (cursor.moveToFirst()) {
            //Get the drink details from the cursor
            while (!cursor.isAfterLast()) {
                Log.d("Found announcement",cursor.getString(0));
                String repo_id = cursor.getString(0);
                String name = cursor.getString(1);
                String current_branch = cursor.getString(2);
                String current_sha = cursor.getString(3);
                Repo temp = new Repo(name,repo_id,current_branch,current_sha);
                repos.add(temp);
                cursor.moveToNext();
            }
        }
        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return repos;
    }

}
