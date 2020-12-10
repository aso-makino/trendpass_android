package com.example.trendpass;

import android.content.Context;
import android.content.Intent;
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
    private String[] spotIdList;

    static class ViewHolder {
        ImageView spotImage;
        TextView spotName;
    }

    public NearSpotListAdapter(Context context, int itemLayoutId,
                               String[] spotNames,String[] spotImages, String[] spotIds) {

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        this.context = context;

        spotNameList = spotNames;
        spotImageList = spotImages;
        spotIdList = spotIds;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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

                //スポット画像
                Picasso.with(context)
                        .load(addUrl(position))
                        .resize(500,500)
                        .placeholder(R.drawable.noimage)
                        .centerCrop()
                        .into((ImageView) convertView.findViewById(R.id.spotImg));

        holder.spotName.setText(spotNameList[position]);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,InsertReviewActivity.class);
                intent.putExtra("spotId", spotIdList[position]);
                intent.putExtra("spotName", spotNameList[position]);
                intent.putExtra("spotImage", spotImageList[position]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    // ネットワークアクセスするURLを設定する
    private String addUrl(int number){

        String ip = "192.168.2.103";
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
