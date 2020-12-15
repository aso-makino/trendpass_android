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

public class AsyncMyPageSpotActivity extends AsyncBaseActivity {

    private String url = null;

    public AsyncMyPageSpotActivity(Activity activity) {
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
        String latitude = "";
        String longitude = "";
        String genreId = "";
        String spotImage = "";


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
            int spotSize = resJson.getInt("spotSize");
            final String[] image = new String[spotSize];

            List<HashMap<String,String>> spotList = new ArrayList<>();
            for(int i = 0; i < spotSize; i++){
                HashMap<String,String> spot = new HashMap<String,String>();

                spotId = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotId");
                spotName = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotName");
                latitude = resJson.getJSONArray("spotList").getJSONObject(i).getString("latitude");
                longitude = resJson.getJSONArray("spotList").getJSONObject(i).getString("longitude");
                genreId = resJson.getJSONArray("spotList").getJSONObject(i).getString("genreId");
                spotImage = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotImage");

                spot.put("spotId",spotId);
                spot.put("spotName",spotName);
                spot.put("latitude",latitude);
                spot.put("longitude",longitude);
                spot.put("genreId",genreId);
                spot.put("reviewImage",spotImage);

                spotList.add(spot);
                image[i] = spotImage;
            }

            // GridViewのインスタンスを生成
            GridView gridview = activity.findViewById(R.id.gridview);
            // BaseAdapter を継承したGridAdapterのインスタンスを生成
            // 子要素のレイアウトファイル grid_items.xml を
            // activity_main.xml に inflate するためにGridAdapterに引数として渡す
            GridAdapter adapter = new GridAdapter(
                    activity.getApplicationContext(),
                    R.layout.mypage_layout,
                    image,
                    spotList
            );

            // gridViewにadapterをセット
            gridview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


