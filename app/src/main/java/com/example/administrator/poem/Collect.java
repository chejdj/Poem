package com.example.administrator.poem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Collect extends Activity {
    private ListView listView;
    private ArrayAdapter<String> mArrayAdapter;
    private DB_Helper helper;
    private Typeface  typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        helper = new DB_Helper(this, null, null, 1);
        listView = (ListView) findViewById(R.id.list_view);
        View text =this.getLayoutInflater().inflate(R.layout.list_item,null);
        TextView view =text.findViewById(R.id.text_title);
        typeface= Typeface.createFromAsset(getAssets(), "fonts/font_ksj.ttf");
        view.setTypeface(typeface);
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item);
        listView.setAdapter(mArrayAdapter);
        FindCollect();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String temp = (String) ((TextView) arg1).getText();
                Intent intent = new Intent();
                intent.putExtra("title", temp);
                intent.setClass(Collect.this, Collect_item.class);
                startActivity(intent);
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    void FindCollect() {
        SQLiteDatabase database = helper.opendatabase();
        Cursor cursor = database.rawQuery("Select * From poem where ticai = 1", null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("mingcheng"));
                mArrayAdapter.add(title);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mArrayAdapter.clear();
        FindCollect();
        mArrayAdapter.notifyDataSetChanged();
    }
}
