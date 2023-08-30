package com.example.social.extra;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperSqllite extends SQLiteOpenHelper {

    // Define your SQLite database helper class

        private static final String DATABASE_NAME = "profile_pics.db";
        private static final int DATABASE_VERSION = 1;

        private static final String TABLE_PROFILE_PICS = "profile_pics";
        private static final String COLUMN_USER_ID = "user_id";
        private static final String COLUMN_IMAGE_PATH = "image_path";

    public DBHelperSqllite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Constructor and other methods

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_PROFILE_PICS +
                    "(" + COLUMN_USER_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_IMAGE_PATH + " TEXT)";
            db.execSQL(createTableQuery);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Perform any necessary upgrades when the database version changes
        // This method is called when DATABASE_VERSION is increased
        // You can drop and recreate tables, update data, etc.

    }

    // Other methods


    // Inserting a profile picture into the SQLite table
    public void insertProfilePicture(String userId, String imagePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_IMAGE_PATH, imagePath);
        db.insert(TABLE_PROFILE_PICS, null, values);
        db.close();
    }

    // Retrieving a profile picture from SQLite
    @SuppressLint("Range")
    public String getProfilePicturePath(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = { COLUMN_IMAGE_PATH };
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { userId };
        Cursor cursor = db.query(TABLE_PROFILE_PICS, columns, selection, selectionArgs, null, null, null);

        String imagePath = null;
        if (cursor.moveToFirst()) {
            imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));
        }

        cursor.close();
        db.close();
        return imagePath;
    }

    @SuppressLint("Range")
    public byte[] getProfilePicture(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_IMAGE_PATH};
        String selection = COLUMN_USER_ID + " = ?";

        if (userId != null) {
            String[] selectionArgs = {userId};
            Cursor cursor = db.query(TABLE_PROFILE_PICS, columns, selection, selectionArgs, null, null, null);

            byte[] imageBytes = null;
            if (cursor.moveToFirst()) {
                imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PATH));
            }

            cursor.close();
            db.close();
            return imageBytes;
        } else {
            db.close();
            return null; // Or handle the situation as needed
        }
    }
}
