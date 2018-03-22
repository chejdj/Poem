package com.example.administrator.poem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Collect_item extends Activity {
    private String ID;
    private DB_Helper helper;
    private Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_item);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        TextView textView = (TextView) findViewById(R.id.poem_item);
        typeface= Typeface.createFromAsset(getAssets(), "fonts/font_ksj.ttf");
        textView.setTypeface(typeface);
        helper = new DB_Helper(this, null, null, 1);
        SQLiteDatabase database = helper.opendatabase();
        Cursor cursor = database.rawQuery("Select * From poem where mingcheng=" + "\"" + title + "\"", null);
        cursor.moveToFirst();
        ID = cursor.getString(cursor.getColumnIndex("_id"));
        String poem = cursor.getString(1) + "\n" + "\n" + cursor.getString(2) + "\n" + "\n" + cursor.getString(13);
        textView.setText(poem);
        cursor.close();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collect_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuInflater inflater = getMenuInflater();
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                try {
                    LayoutInflater f = getLayoutInflater();
                    final View view = f.createView(name, null, attrs);
                    if (view instanceof TextView) {
                        ((TextView) view).setTypeface(typeface);
                        ((TextView) view).setTextSize(18.0f);
                    }
                    return view;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        int id = item.getItemId();
        if (id == R.id.dislike_collect) {
            SQLiteDatabase db = helper.opendatabase();
            db.execSQL("UPDATE poem SET ticai = 0 WHERE _id =" + ID);
            db.close();
            Toast.makeText(getApplicationContext(),
                    "取消成功",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
