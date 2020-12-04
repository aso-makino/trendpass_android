package com.example.trendpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class NearSpotListAdapter extends BaseAdapter {

    private final Context context;
    private LayoutInflater inflater;
    private int layoutID;
    private String[] spotNameList;
    private String[] spotImageList;

    static class ViewHolder {
        ImageView spotImage;
        TextView spotName;
    }

    public NearSpotListAdapter(Context context, int itemLayoutId,
                               String[] spotNames,String[] spotImages) {

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        this.context = context;

        spotNameList = spotNames;
        spotImageList = spotImages;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.spotImage = convertView.findViewById(R.id.spotImg);
            holder.spotName = convertView.findViewById(R.id.spotName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        System.out.println(spotImageList[position]);

        String ip ="";
                //スポット画像
                Picasso.with(context)
                        .load(addUrl(position))
                        .resize(500,500)
                        .placeholder(R.drawable.noimage)
                        .centerCrop()
                        .into((ImageView) convertView.findViewById(R.id.spotImg));

        holder.spotName.setText(spotNameList[position]);


        return convertView;
    }

    // ネットワークアクセスするURLを設定する
    private String addUrl(int number){

        String ip = "";
        return String.format(Locale.US,
                "http://" + ip + ":8080/trendpass/DisplayImage?name=%s" ,// 自分のサーバーに上げて見ましょう
                spotImageList[number]);
    }



    @Override
    public int getCount() {
        // 全要素数を返す
        return spotImageList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
