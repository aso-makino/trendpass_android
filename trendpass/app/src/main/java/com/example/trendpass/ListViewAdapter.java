package com.example.trendpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    //scenes, datetexts, memos, latitude, longitude
    private String[] scenes;
    private String[] datatexts;
    private String[] memos;
    private String[] latitude;
    private String[] longitude;

    ListViewAdapter(Context context, int itemLayoutId,
                    String[] scenes) {
        //String[] scenes,
        //String[] datatexts,
        //String[] memos,
        //String[] latitude,
        //String[] longitude){
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        //this.scenes = scenes;//位置情報
        //this.datetexts = datetests;//日付
        //this.memos = memos;//メモ
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            // activity_main.xml に list.xml を inflate して convertView とする
            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.textView.setText(scenes[position]);
        // 現在の position にあるファイル名リストを holder の textView にセット
        //holder.textView.setText(datetexts[position]);
        // 現在の position にあるファイル名リストを holder の textView にセット
        //holder.textView.setText(memos[position]);


        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return scenes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}