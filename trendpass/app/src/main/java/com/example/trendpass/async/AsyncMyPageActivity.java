package com.example.trendpass.async;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trendpass.GridAdapter;
import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncMyPageActivity extends AsyncBaseActivity {

    public AsyncMyPageActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls);
        System.out.println("doInback:::::::::::::::::" + resJson);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {

        String spotId = "";
        String spotName = "";
        String latitude = "";
        String longitude = "";
        String genreId = "";

        String reviewNumber = "";
        String reviewImage = "";

        try {
            int spotSize = resJson.getInt("spotSize");
            int reviewSize = resJson.getInt("reviewSize");
//
//            for(int i = 0; i < spotSize; i++){
//                JSONObject spotList = resJson.getJSONArray("spotList").getJSONObject(i);
//                spotId = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotId");
//                spotName = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotName");
//                latitude = resJson.getJSONArray("spotList").getJSONObject(i).getString("ratitude");
//                longitude = resJson.getJSONArray("spotList").getJSONObject(i).getString("longitude");
//                genreId = resJson.getJSONArray("spotList").getJSONObject(i).getString("genreId");
//
//            }

            List<HashMap<String,String>> spotReviewList = new ArrayList<HashMap<String, String>>();
            final String[] image = new String[reviewSize];

            for(int i = 0; i < reviewSize; i++){

                HashMap<String,String> spotReview = new HashMap<String,String>();

                spotId = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("spotId");
                spotName = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("spotName");
                latitude = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("ratitude");
                longitude = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("longitude");
                genreId = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("genreId");
                reviewNumber = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("reviewNumber");
                reviewImage = resJson.getJSONArray("spotReviewList").getJSONObject(i).getString("reviewImage");

                spotReview.put("spotId",spotId);
                spotReview.put("spotName",spotName);
                spotReview.put("latitude",latitude);
                spotReview.put("longitude",longitude);
                spotReview.put("genreId",genreId);
                spotReview.put("reviewNumber",reviewNumber);
                spotReview.put("reviewImage",reviewImage);

                spotReviewList.add(spotReview);

                image[i] = reviewImage;
            }

            System.out.println(3);
            // GridViewのインスタンスを生成
            GridView gridview = activity.findViewById(R.id.gridview);
            // BaseAdapter を継承したGridAdapterのインスタンスを生成
            // 子要素のレイアウトファイル grid_items.xml を
            // activity_main.xml に inflate するためにGridAdapterに引数として渡す
            GridAdapter adapter = new GridAdapter(
                    activity.getApplicationContext(),
                    R.layout.mypage_layout,
                    image
            );

            System.out.println(4);
            // gridViewにadapterをセット
            gridview.setAdapter(adapter);
            System.out.println("まきの");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        if (result == false){
            TextView tv = activity.findViewById(R.id.LoginResultTxtv);
            tv.setText("メールアドレス又は、パスワードが間違っています。");
        }
        */
    }
}

