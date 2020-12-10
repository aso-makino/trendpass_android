package com.example.trendpass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RatingAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutID;
    private List<HashMap<String,String>> reviewList;
    private HashMap<String, String> spot;
    private Context context;

    static class ViewHolder {
        RatingBar reviewRating;
        TextView reviewContent;
    }

    public RatingAdapter(Context context, int itemLayoutId,
                         List<HashMap<String, String>> reviewList,HashMap<String, String> spot){

        super();
        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        this.reviewList = reviewList;
        this.spot = spot;
        this.context = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        System.out.println(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.reviewRating = convertView.findViewById(R.id.reviewRatingBar);
            holder.reviewContent = convertView.findViewById(R.id.reviewContent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.reviewRating.setRating(Integer.parseInt(reviewList.get(position).get("rating")));

        holder.reviewContent.setText(reviewList.get(position).get("reviewContent"));

            convertView.findViewById(R.id.review).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

                    Intent intent = new Intent(context, ReviewDetailActivity.class);
                    intent.putExtra("spotId", spot.get("spotId"));
                    intent.putExtra("reviewNumber", reviewList.get(position).get("reviewNumber"));
                    intent.putExtra("reviewImage", reviewList.get(position).get("reviewImage"));
                    intent.putExtra("reviewContent", reviewList.get(position).get("reviewContent"));
                    intent.putExtra("rating", reviewList.get(position).get("rating"));
                    intent.putExtra("spotName", spot.get("spotName"));
                    intent.putExtra("reviewUserId", reviewList.get(position).get("reviewUserId"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });



        return convertView;
    }

    @Override
    public int getCount() {
        // 全要素数を返す
        return reviewList.size();
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
