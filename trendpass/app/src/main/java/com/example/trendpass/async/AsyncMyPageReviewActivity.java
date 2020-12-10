package com.example.trendpass.async;

import android.app.Activity;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.trendpass.GridAdapter;
import com.example.trendpass.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncMyPageReviewActivity extends AsyncBaseActivity {

    private String url = null;

    public AsyncMyPageReviewActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        this.url = urls[0].toString();
        JSONObject resJson = super.doInBackground(urls);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {

        String spotId = "";
        String spotName = "";
        String genreId = "";

        String reviewNumber = "";
        String reviewImage = "";

        String userId = "";
        String userName = "";
        String userIcon = "";

        // ユーザーの情報
        try {
            userId = resJson.getJSONObject("userInfo").getString("userId");
            userName = resJson.getJSONObject("userInfo").getString("userName");
            userIcon = resJson.getJSONObject("userInfo").getString("userIcon");

            //　ユーザー名表示
            TextView userNameTxv = activity.findViewById(R.id.userNameTxv);
            userNameTxv.setText(userName);

            //　ユーザーアイコン表示
            Picasso.with(activity.getApplicationContext())
                    .load(url.replace("/MyPageServlet?userId=" + userId,"/DisplayImage?name=" + userIcon))
                            .resize(500,500)
                            .placeholder(R.drawable.user)
                            .centerInside()
                            .into((ImageView) activity.findViewById(R.id.user_icon));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //　投稿の情報
        try {
            int reviewSize = resJson.getInt("reviewSize");

            final String[] image = new String[reviewSize];

            System.out.println(resJson);
            List<HashMap<String,String>> spotList = new ArrayList<>();
            for(int i = 0; i < reviewSize; i++){

                HashMap<String,String> spotReview = new HashMap<String,String>();

                spotId = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("spotId");
                spotName = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("spotName");
                genreId = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("genreId");
                reviewNumber = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("reviewNumber");
                reviewImage = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("reviewImage");

                spotReview.put("spotId",spotId);
                spotReview.put("spotName",spotName);
                spotReview.put("genreId",genreId);
                spotReview.put("reviewNumber",reviewNumber);
                spotReview.put("reviewImage",reviewImage);

                spotList.add(spotReview);
                image[i] = reviewImage;
            }

            System.out.println(3);
            // GridViewのインスタンスを生成
            GridView gridview = activity.findViewById(R.id.gridview);
            // BaseAdapter を継承したGridAdapterのインスタンスを生成
            // 子要素のレイアウトファイル mypage_layout.xml を
            // activity_my_page.xml に inflate するためにGridAdapterに引数として渡す
            GridAdapter adapter = new GridAdapter(
                    activity.getApplicationContext(),
                    R.layout.mypage_layout,
                    image,
                    spotList
            );

            System.out.println(4);
            // gridViewにadapterをセット
            gridview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


