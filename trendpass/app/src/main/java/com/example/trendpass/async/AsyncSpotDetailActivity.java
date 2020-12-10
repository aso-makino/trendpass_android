package com.example.trendpass.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.trendpass.DispSpotDetailActivity;
import com.example.trendpass.R;
import com.example.trendpass.RatingAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncSpotDetailActivity extends AsyncBaseActivity {

    private String url = null;

    private String userId = "";
    private String spotId = "";
    private String spotName = "";
    private String genreId = "";
    private String reviewNumber = "";
    private String reviewImage = "";
    private String reviewContent = "";
    private int rating = 0;

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

        System.out.println(resJson);

        int ratingAverage = 0;

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

            //　ユーザーIDを取得
            SharedPreferences loginData = activity.getSharedPreferences("login_data", activity.MODE_PRIVATE);
            final String userId = loginData.getString("userId", "");


            //削除ボタン表示するかの判定
            Button deleteBtn = (Button) activity.findViewById(R.id.delete_button);
            if(userId.equals(resJson.getJSONObject("spot").getString("userId"))) {
                deleteBtn.setVisibility(View.VISIBLE);
            }else{
                deleteBtn.setVisibility(View.INVISIBLE);

            }


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

            //口コミ件数
            int reviewCount = Integer.parseInt(resJson.getString("reviewCount"));

            //スポット情報
            HashMap<String,String> spot = new HashMap<String,String>();
            spotId = resJson.getJSONObject("spot").getString("spotId");
            spotName = resJson.getJSONObject("spot").getString("spotName");
            genreId = resJson.getJSONObject("spot").getString("genreId");

            spot.put("spotId",spotId);
            spot.put("spotName",spotName);
            spot.put("genreId",genreId);

            //口コミ情報
            List<HashMap<String,String>> reviewList = new ArrayList<>();
            for(int i = 0; i < reviewCount; i++){

                HashMap<String,String> review = new HashMap<String,String>();


                reviewNumber = resJson.getJSONArray("review").getJSONObject(i).getString("reviewNumber");
                reviewImage = resJson.getJSONArray("review").getJSONObject(i).getString("reviewImage");
                reviewContent = resJson.getJSONArray("review").getJSONObject(i).getString("reviewContent");
                rating = Integer.parseInt(resJson.getJSONArray("review").getJSONObject(i).getString("evaluation"));
                this.userId = resJson.getJSONArray("review").getJSONObject(i).getString("userId");



                review.put("reviewNumber",reviewNumber);
                review.put("reviewImage",reviewImage);
                review.put("reviewContent",reviewContent);
                review.put("rating",String.valueOf(rating));
                review.put("reviewUserId",this.userId);

                reviewList.add(review);

                ratingAverage += rating;
                }

            //レイティングバー
            ratingAverage = ratingAverage/reviewCount;
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
            ratingBar.setRating(ratingAverage);

            // ListViewのインスタンスを生成
            ListView listView = activity.findViewById(R.id.reviewList);

            // BaseAdapter を継承したadapterのインスタンスを生成
            BaseAdapter adapter = new RatingAdapter(activity.getApplicationContext(),
                    R.layout.list_disp_spot_detail, reviewList,spot);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);

            //削除ボタンをタッチした時の処理
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                //ボタンタッチしたら確認ダイアログ
                public void onClick(View view) {

                    new AlertDialog.Builder(activity)
                            .setTitle( "スポット削除確認" )
                            .setMessage( "スポットを削除します。\nよろしいですか" )
                            .setIcon( R.drawable.rogo )
                            .setPositiveButton( "削除", new  DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // クリックしたときの処理

                                    AsyncSpotDeleteActivity asyncSpotDeleteActivity = new AsyncSpotDeleteActivity(activity);
                                    try {
                                        asyncSpotDeleteActivity.execute(new URL(url.replace("DispSpotDetail?spotId="+resJson.getJSONObject("spot").getString("spotId"),"DeleteSpotServlet?spotId=" + spotId+"&userId="+userId)));
                                    } catch (MalformedURLException | JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            })
                            .setNegativeButton("キャンセル", new  DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // クリックしたときの処理
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}