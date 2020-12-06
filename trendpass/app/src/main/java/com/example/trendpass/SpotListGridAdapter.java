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

public class SpotListGridAdapter extends BaseAdapter {

    private List<HashMap<String,String>> spotReviewList;
    private LayoutInflater inflater;
    private int layoutId;
    private Context context;
    private List<String> imageList = new ArrayList<>();

    public SpotListGridAdapter(Context context,
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

        ImageView img = view.findViewById(R.id.mypage_image);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);



        System.out.println(1);
        Picasso.with(context)
                .load(addUrl(position))
                .resize(500, 500)
                .placeholder(R.drawable.insert)
                .error(R.drawable.map)
                .into(img);


        view.findViewById(R.id.mypage_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = imageList.get(position) + "が選択されました。";
                System.out.println(message);
            }
        });

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
