package com.example.trendpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RankingGridAdapter extends BaseAdapter {

    private List<HashMap<String,String>> spotReviewList;
    private LayoutInflater inflater;
    private int layoutId;
    private Context context;
    private List<String> imageList = new ArrayList<>();

    public RankingGridAdapter(Context context,
                       int layoutId,
                       //                   List<HashMap<String, String>> spotReviewList) {
                       String[] image) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        // this.spotReviewList = spotReviewList;
        this.context = context;
        Collections.addAll(imageList, image);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = inflater.inflate(layoutId, parent, false);
        } else {
            view =  convertView;
        }

        ImageView spotImg = view.findViewById(R.id.spot_image);
        ImageView rankingImg = view.findViewById(R.id.ranking_image);
        if(position == 0) {
            spotImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(context)
                    .load(addUrl(position))
                    .resize(500, 500)
                    .placeholder(R.drawable.insert)
                    .error(R.drawable.noimage)
                    .into(spotImg);

            Picasso.with(context)
                    .load(R.drawable.ranking1)
                    .resize(500, 500)
                    .placeholder(R.drawable.insert)
                    .error(R.drawable.noimage)
                    .into(rankingImg);


            view.findViewById(R.id.spot_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = position + "位の" + imageList.get(position) + "が選択されました。";
                    System.out.println(message);
                }
            });
        }else if(position == 1){

            spotImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(context)
                    .load(addUrl(position))
                    .resize(500, 500)
                    .placeholder(R.drawable.insert)
                    .error(R.drawable.noimage)
                    .into(spotImg);

            Picasso.with(context)
                    .load(R.drawable.ranking2)
                    .resize(500, 500)
                    .placeholder(R.drawable.insert)
                    .error(R.drawable.noimage)
                    .into(rankingImg);


            view.findViewById(R.id.spot_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = position + "位の" + imageList.get(position) + "が選択されました。";
                    System.out.println(message);
                }
            });

        }else if(position == 2){

            spotImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(context)
                    .load(addUrl(position))
                    .resize(500, 500)
                    .placeholder(R.drawable.insert)
                    .error(R.drawable.noimage)
                    .into(spotImg);

            Picasso.with(context)
                    .load(R.drawable.ranking3)
                    .resize(500, 500)
                    .placeholder(R.drawable.insert)
                    .error(R.drawable.noimage)
                    .into(rankingImg);


            view.findViewById(R.id.spot_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = position + "位の" + imageList.get(position) + "が選択されました。";
                    System.out.println(message);
                }
            });

        }else{

            spotImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(context)
                    .load(addUrl(position))
                    .resize(500, 500)
                    .placeholder(R.drawable.insert)
                    .error(R.drawable.noimage)
                    .into(spotImg);

            view.findViewById(R.id.spot_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = position + "位の" + imageList.get(position) + "が選択されました。";
                    System.out.println(message);
                }
            });

        }

        return view;
    }

    // ネットワークアクセスするURLを設定する
    private String addUrl(int number){

        String ip = "";
        return String.format(Locale.US,
                "http://" + ip + ":8080/trendpass/DisplayImage?name=%s" ,// 自分のサーバーに上げて見ましょう
                imageList.get(number));
    }


    @Override
    public int getCount() {
        // 全要素数を返す
        return imageList.size();
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
