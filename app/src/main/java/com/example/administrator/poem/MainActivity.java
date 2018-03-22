package com.example.administrator.poem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.widget.SwipeRefreshLayout;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends Activity {
    private final int BUFFER_SIZE = 400000;
    private static final String DB_NAME = "poem_all.db";
    private static final String PACKAGE_NAME = "com.chejdj.com";
    private static final String DB_PATH =
            Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/databases";
    private TextView poem_text;
    private SwipeRefreshLayout layout;
    private RelativeLayout main;
    private static  int ID;
    private DB_Helper helper;
    private Typeface tpeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        poem_text = (TextView) findViewById(R.id.poem_content);
        main=(RelativeLayout)findViewById(R.id.main);
        helper=new DB_Helper(this,null,null,1);
        FindaPoem();
        tpeface = Typeface.createFromAsset(getAssets(), "fonts/font_ksj.ttf");
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
        ID = (int) (1 + Math.random() * (59170));
        SQLiteDatabase db = helper.opendatabase();
        if (db != null) {
            // 查找语句
            Cursor cursor = db.rawQuery("Select * From poem Where _id=" + ID, null);
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
    private void AddPoemToCollected(){
        SQLiteDatabase db = helper.opendatabase();
        db.execSQL("UPDATE poem SET ticai = 1 WHERE _id ="+ID);
        db.close();
        Toast.makeText(getApplicationContext(),
                "收藏成功",
                Toast.LENGTH_SHORT).show();
    }
    private void DeletePoemFromCollected(){
        SQLiteDatabase db = helper.opendatabase();
        db.execSQL("UPDATE poem SET ticai = 0 WHERE _id ="+ID);
        db.close();
        Toast.makeText(getApplicationContext(),
                "取消成功",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                try {
                    LayoutInflater f = getLayoutInflater();
                    final View view = f.createView(name, null, attrs);
                    if (view instanceof TextView) {
                        ((TextView) view).setTypeface(tpeface);
                        ((TextView) view).setTextSize(18.0f);
                    }
                    return view;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.collect:
                Intent intent = new Intent(this,Collect.class);
                startActivity(intent);
                break;
            case R.id.like:
                AddPoemToCollected();
                break;
            case R.id.dislike:
                DeletePoemFromCollected();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
