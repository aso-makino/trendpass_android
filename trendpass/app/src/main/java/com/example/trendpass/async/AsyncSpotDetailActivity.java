package com.example.trendpass.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.trendpass.R;
import com.example.trendpass.RatingAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class AsyncSpotDetailActivity extends AsyncBaseActivity {

    private String url = null;
    private int reviewRating[];
    private String reviewContent[];
    private String reviewImage[];
    private  String spotName = "";

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
    protected void onPostExecute(final JSONObject resJson) {

        int rating = 0;

        try {

            activity.findViewById(R.id.route_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Uri location = Uri.parse("geo:"+resJson.getJSONObject("spot").getString("longitude")+","+resJson.getJSONObject("spot").getString("ratitude")+"?q="+
                                resJson.getJSONObject("spot").getString("longitude")+","+resJson.getJSONObject("spot").getString("ratitude")+"("+resJson.getJSONObject("spot").getString("spotName")+")");
                        System.out.println(location);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

                        PackageManager packageManager = activity.getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                        boolean isIntentSafe = activities.size() > 0;

                        if (isIntentSafe) {
                            activity.startActivity(mapIntent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


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
            spotNametv.setTextSize(22.0f);

            //口コミ
            int reviewCount = Integer.parseInt(resJson.getString("reviewCount"));


            reviewImage = new String[reviewCount];
            reviewRating = new int[reviewCount];
            reviewContent = new String[reviewCount];

            for (int i = 0 ; i<reviewCount;i++) {

                reviewImage[i] = resJson.getJSONArray("review").getJSONObject(i).getString("reviewImage");
                reviewRating[i] = Integer.parseInt(resJson.getJSONArray("review").getJSONObject(i).getString("evaluation"));
                reviewContent[i] = resJson.getJSONArray("review").getJSONObject(i).getString("reviewContent");
                rating += reviewRating[i];
                }

            //レイティングバー
            rating = rating/reviewCount;
            RatingBar ratingBar = (RatingBar) activity.findViewById(R.id.ratingBar);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                try {
                    Drawable progressDrawable = ratingBar.getProgressDrawable();
                    if (progressDrawable != null) {
                        DrawableCompat.setTint(progressDrawable, ContextCompat.getColor(ratingBar.getContext(), R.color.ratingbar));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ratingBar.setRating(rating);

            // ListViewのインスタンスを生成
            ListView listView = activity.findViewById(R.id.reviewList);

            // BaseAdapter を継承したadapterのインスタンスを生成
            BaseAdapter adapter = new RatingAdapter(activity.getApplicationContext(),
                    R.layout.list_disp_spot_detail, reviewRating,reviewContent);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getReviewImage(int position){
        return reviewImage[position];
    }
    public int getReviewRating(int position){
        return reviewRating[position];
    }
    public String getReviewContent(int position){
        return reviewContent[position];
    }
    public String getSpotName(){
        return spotName;
    }
}