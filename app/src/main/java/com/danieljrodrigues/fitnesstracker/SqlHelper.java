package com.danieljrodrigues.fitnesstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SqlHelper extends SQLiteOpenHelper {
    private static SqlHelper INSTANCE;
    private static final String DB_NAME = "fitness_tracker.db";
    private static final int DB_VERSION = 1;

    private SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SqlHelper getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new SqlHelper(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE calc (id INTEGER PRIMARY KEY, type_calc TEXT, res DECIMAL, created_at DATETIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addItem(String type, double response) {
        SQLiteDatabase db = getWritableDatabase();

        // Id of the lasted value added, return -1 if didn't work
        long calcId = 0;

        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("pt", "BR"));
            String currentDate = sdf.format(new Date());

            values.put("type_calc", type);
            values.put("res", response);
            values.put("created_at", currentDate);

            calcId = db.insertOrThrow("calc", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if (db.isOpen()) {
                db.endTransaction();
            }
        }
        return calcId;
    }
}
