package com.example.administrator.poem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/26.
 */

public class collect_adapter extends RecyclerView.Adapter<collect_adapter.CollectHolder> {
    private ArrayList<String> mPoem;
    private OnItemClickListener mOnItemClickListenre;

    public interface OnItemClickListener {
        void onClick(int position, String poem);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListenre = onItemClickListener;
    }

    public collect_adapter(ArrayList<String> mPoem) {
        this.mPoem = mPoem;
    }

    static class CollectHolder extends RecyclerView.ViewHolder {
        public TextView textview;

        public CollectHolder(View view) {
            super(view);
            this.textview = (TextView) view.findViewById(R.id.text_title);
        }
    }

    @Override
    public CollectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CollectHolder holder = new CollectHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CollectHolder holder, final int position) {
        String str = (String) mPoem.get(position);
        holder.textview.setText(str);
        if (mOnItemClickListenre != null) {
            holder.textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListenre.onClick(position, holder.textview.getText().toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPoem.size();
    }
}
