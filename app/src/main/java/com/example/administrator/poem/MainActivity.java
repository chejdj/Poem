package com.example.administrator.poem;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    private final int BUFFER_SIZE = 400000;
    private static final String DB_NAME = "poem_all.db";
    private static final String PACKAGE_NAME = "com.chejdj.com";
    private static final String DB_PATH =
            Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/databases";
    private TextView poem_text;
    private SwipeRefreshLayout layout;
    private RelativeLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        poem_text = (TextView) findViewById(R.id.poem_content);
        main=(RelativeLayout)findViewById(R.id.main);
        FindaPoem();
        Typeface tpeface = Typeface.createFromAsset(getAssets(), "fonts/font_ksj.ttf");
        poem_text.setTypeface(tpeface);
        layout.setColorScheme(R.color.bag_poem);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(true);
                        FindaPoem();
                        layout.setRefreshing(false);
                    }
                }, 500);
            }
        });

    }

    private void FindaPoem() {
        int id = (int) (1 + Math.random() * (59170));
        SQLiteDatabase db = opendatabase();
        if (db != null) {
            // 查找语句
            Cursor cursor = db.rawQuery("Select * From poem Where _id=" + id, null);
            cursor.moveToFirst();
          String content = cursor.getString(1) + "\n" + "\n" + cursor.getString(2) +
                    "\n" + "\n" + cursor.getString(13);
            ChangeBackground();
            poem_text.setText(content);
            cursor.close();
            db.close();
        }
    }
    private void ChangeBackground(){
        int ln = (int) (1 + Math.random() * (3));
        switch (ln){
            case 1:
                main.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_1));
                break;
            case 2:
                main.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_2));
                break;
            case 3:
                main.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_3));
                break;
        }
    }
    // 这里是直接在文件中查找数据库，并没有创建数据库
    private SQLiteDatabase opendatabase() {
        File myDataPath = new File(DB_PATH);
        if (!myDataPath.exists()) {
            Log.e("DB", "create file");
            myDataPath.mkdirs();
        }
        String dbfile = DB_PATH + "/" + DB_NAME;
        File db = new File(dbfile);
        try {
            if (!db.exists()) {
                InputStream in = this.getResources().openRawResource(R.raw.poem_all);
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
}
