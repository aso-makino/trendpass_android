package com.example.trendpass.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.trendpass.R;
import com.example.trendpass.RatingAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncSpotDetailActivity extends AsyncBaseActivity {

    private String url = null;

    public AsyncSpotDetailActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        this.url = urls[0].toString();
        JSONObject resJson = super.doInBackground(urls);
        return resJson;
    }

    @SuppressLint("ResourceType")
    protected void onPostExecute(JSONObject resJson) {

        String spotName = "";
        int rating = 0;

        try {

            //スポット画像
            Picasso.with(activity.getApplicationContext())
                    .load(url.replace("DispSpotDetail?spotId="+resJson.getJSONObject("spot").getString("spotId"),"DisplayImage?name="+resJson.getJSONArray("review").getJSONObject(0).getString("reviewImage")))
                    .resize(500,500)
                    .placeholder(R.drawable.noimage)
                    .centerInside()
                    .into((ImageView) activity.findViewById(R.id.spotImg));

            //スポット名
            spotName = resJson.getJSONObject("spot").getString("spotName");
            TextView spotNametv = activity.findViewById(R.id.spotName);
            spotNametv.setText(spotName);

            //口コミ
            int count = Integer.parseInt(resJson.getString("reviewCount"));

            int[] reviewRating = new int[count];
            String[] reviewContent = new String[count];

            for (int i = 0 ; i<count;i++) {

                //レイティングバー
                reviewRating[i] = Integer.parseInt(resJson.getJSONArray("review").getJSONObject(i).getString("evaluation"));
                reviewContent[i] = resJson.getJSONArray("review").getJSONObject(i).getString("reviewContent");
                rating += reviewRating[i];
                }

            //レイティングバー
            rating = rating/count;
            RatingBar ratingBar = (RatingBar) activity.findViewById(R.id.ratingBar);
            ratingBar.setRating(rating);

            TextView ratingtv = activity.findViewById(R.id.ratingStr);
            ratingtv.setText(String.valueOf(rating));

            // ListViewのインスタンスを生成
            ListView listView = activity.findViewById(R.id.reviewList);

            // BaseAdapter を継承したadapterのインスタンスを生成
            // レイアウトファイル list_items.xml を
            // activity_main.xml に inflate するためにadapterに引数として渡す
            BaseAdapter adapter = new RatingAdapter(activity.getApplicationContext(),
                    R.layout.list_disp_spot_detail, reviewRating,reviewContent);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}