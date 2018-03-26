package com.example.administrator.poem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Collect extends Activity {
    private RecyclerView recyclerView;
    private DB_Helper helper;
    private collect_adapter adapter;
    private Typeface typeface;
    private ArrayList<String> content = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        helper = new DB_Helper(this, null, null, 1);
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        View text = this.getLayoutInflater().inflate(R.layout.list_item, null);
        TextView view = text.findViewById(R.id.text_title);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/font_ksj.ttf");
        view.setTypeface(typeface);
        adapter = new collect_adapter(content);
        FindCollect();
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new collect_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String str) {
                Intent intent = new Intent();
                intent.putExtra("title", str);
                intent.setClass(Collect.this, Collect_item.class);
                startActivity(intent);
            }
        });
    }

    void FindCollect() {
        SQLiteDatabase database = helper.opendatabase();
        Cursor cursor = database.rawQuery("Select * From poem where ticai = 1", null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("mingcheng"));
                content.add(title);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        content.clear();
        FindCollect();
        adapter.notifyDataSetChanged();
    }
}
