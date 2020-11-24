package com.example.trendpass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatingAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutID;
    private int[] reviewRatingList;
    private String[] reviewContentList;

    static class ViewHolder {
        RatingBar reviewRating;
        TextView reviewContent;
    }

    public RatingAdapter(Context context, int itemLayoutId,
                         int[] reviewRatings, String[] reviewContents){

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;

        reviewRatingList = reviewRatings;
        reviewContentList = reviewContents;

        System.out.println(reviewContents);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

        holder.reviewRating.setRating(reviewRatingList[position]);

        holder.reviewContent.setText(reviewContentList[position]);


        return convertView;
    }

    @Override
    public int getCount() {
        return reviewRatingList.length;
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
