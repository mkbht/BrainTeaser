package com.zorganlabs.brainteaser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.zorganlabs.brainteaser.models.Leaderboard;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String dbName = "Records.db";
    public static final int version = 1;
    public static final String TABLE_NAME = "LeaderBoard";
    public static final String COL1 = "id";
    public static final String COL2 = "category";
    public static final String COL3 = "correct";
    public static final String COL4 = "incorrect";
    public static final String COL5 = "createdAt";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL2 + " TEXT NOT NULL," +
            COL3 + " INT NOT NULL," +
            COL4 + " INT NOT NULL," +
            COL5 + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ");";
    private static  final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHandler(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    // execute create table SQL query
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    // execute drop table and recreate
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    // Add data to grade table
    public boolean insertLeaderBoard(Leaderboard leaderBoard) {
        SQLiteDatabase db = this.getWritableDatabase();
        // getting content values instance
        ContentValues contentValues = new ContentValues();
        // taking content values and putting data into columns
        contentValues.put(COL2, leaderBoard.getCategory());
        contentValues.put(COL3, leaderBoard.getCorrect());
        contentValues.put(COL4, leaderBoard.getIncorrect());
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // view grades
    public Cursor viewLeaderBoard() {
        SQLiteDatabase db = this.getWritableDatabase();
        // query and store on cursor
        Cursor cursor;
        cursor = db.rawQuery("SELECT category, COUNT(category) AS total, SUM(correct) as correct, SUM(incorrect) AS incorrect FROM " + TABLE_NAME + " GROUP BY category" , null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
