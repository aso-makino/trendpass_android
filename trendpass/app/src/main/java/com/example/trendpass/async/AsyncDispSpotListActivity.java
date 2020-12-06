package com.example.trendpass.async;

import android.app.Activity;
import android.widget.GridView;

import com.example.trendpass.GridAdapter;
import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;

public class AsyncDispSpotListActivity extends AsyncBaseActivity {

    private String url = null;

    public AsyncDispSpotListActivity(Activity activity) {
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


        //　投稿の情報
        try {
            int spotSize = resJson.getInt("spotSize");
            final String[] image = new String[spotSize];

            for(int i = 0; i < spotSize; i++){
                HashMap<String,String> spot = new HashMap<String,String>();

                spotId = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotId");
                spotName = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotName");
                latitude = resJson.getJSONArray("spotList").getJSONObject(i).getString("ratitude");
                longitude = resJson.getJSONArray("spotList").getJSONObject(i).getString("longitude");
                genreId = resJson.getJSONArray("spotList").getJSONObject(i).getString("genreId");
                spotImage = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotImage");

                spot.put("spotId",spotId);
                spot.put("spotName",spotName);
                spot.put("latitude",latitude);
                spot.put("longitude",longitude);
                spot.put("genreId",genreId);
                spot.put("reviewImage",spotImage);
                image[i] = spotImage;
            }

            // GridViewのインスタンスを生成
            GridView gridview = activity.findViewById(R.id.gridview);

            // BaseAdapter を継承したGridAdapterのインスタンスを生成
            // 子要素のレイアウトファイル grid_items.xml を
            // activity_main.xml に inflate するためにGridAdapterに引数として渡す
            GridAdapter adapter = new GridAdapter(
                    activity.getApplicationContext(),
                    R.layout.activity_disp_spot_list,
                    image
            );

            // gridViewにadapterをセット
            gridview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


