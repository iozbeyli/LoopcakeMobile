package com.loopcake.loopcakemobile.LCDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loopcake.loopcakemobile.R;

/**
 * Created by Melih on 28.05.2017.
 */

public class LCDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "starbuzz"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    LCDatabaseHelper(Context context) {
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
            db.execSQL("CREATE TABLE USER ("
                    + "USER_ID TEXT PRIMARY KEY, "
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
            db.execSQL("CREATE TABLE COURSE ("
                    + "COURSE_ID TEXT PRIMARY KEY,"
                    + "NAME TEXT"
                    + "CODE TEXT);"
            );
            db.execSQL("CREATE TABLE ANNOUNCEMENT ( "
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "COURSE_ID TEXT,"
                    + "TITLE TEXT,"
                    + "CONTENT TEXT);"
            );
        }
        //insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter);
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        }
    }

    private static void insertDrink(SQLiteDatabase db, String name,
                                    String description, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
    }


}
