package com.example.administrator.poem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/3/22.
 */

public class DB_Helper  extends SQLiteOpenHelper{
    private Context mcontext;
    private final int BUFFER_SIZE = 400000;
    private static final String DB_NAME = "poem_all.db";
    private static final String PACKAGE_NAME = "com.chejdj.com";
    private static final String DB_PATH =
            Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/databases";
    public DB_Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    public  SQLiteDatabase opendatabase() {
        File myDataPath = new File(DB_PATH);
        if (!myDataPath.exists()) {
            Log.e("DB", "create file");
            myDataPath.mkdirs();
        }
        String dbfile = DB_PATH + "/" + DB_NAME;
        File db = new File(dbfile);
        try {
            if (!db.exists()) {
                InputStream in = mcontext.getResources().openRawResource(R.raw.poem_all);
                FileOutputStream out = new FileOutputStream(db);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                }
                out.close();
                in.close();
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return database;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
