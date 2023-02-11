package com.example.androidcrudapplication2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="EmployeeDetails";
    public static final String TABLE_NAME = "Employee";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String EMAIL="email";
    public static final String CONTACT="contact";

    private SQLiteDatabase sqLiteDatabase;

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT NOT NULL," + EMAIL + " TEXT NOT NULL," + CONTACT + " TEXT NOT NULL);";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    public void addEmployee(ModelClass modelClass)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.NAME, modelClass.getName());
        contentValues.put(Database.EMAIL, modelClass.getEmail());
        contentValues.put(Database.CONTACT, modelClass.getContact());
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(Database.TABLE_NAME, null, contentValues);
    }

    public List<ModelClass> viewEmployee()
    {
        String sql = "select * from " + TABLE_NAME;
        sqLiteDatabase = this.getReadableDatabase();
        List<ModelClass> storeEmployee = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String contact = cursor.getString(3);
                storeEmployee.add(new ModelClass(id,name,email,contact));

            }while(cursor.moveToNext());
        }

        cursor.close();
        return storeEmployee;

    }

    public void updateEmployee(ModelClass modelClass)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.NAME, modelClass.getName());
        contentValues.put(Database.EMAIL, modelClass.getEmail());
        contentValues.put(Database.CONTACT, modelClass.getContact());
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update(TABLE_NAME, contentValues, ID + " =?", new String[] {String.valueOf(modelClass.getId())});
    }

    public void deleteEmployee(int id)
    {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, ID + " =?", new String[] {String.valueOf(id)});
    }


}
