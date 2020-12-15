package com.example.trendpass.async;

import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.trendpass.NearSpotListAdapter;
import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncNearSpotListActivity extends AsyncBaseActivity {
    private double latitude[];
    private double longitude[];
    private  String spotName[];
    private  String spotId[];
    private String spotImage[];



    public AsyncNearSpotListActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls);
        return resJson;
    }


    protected void onPostExecute(final JSONObject resJson) {

        try {
            int spotCount = Integer.parseInt(resJson.getString("spotCount"));

            System.out.println(resJson);

            latitude = new double[spotCount];
            longitude = new double[spotCount];
            spotName = new String[spotCount];
            spotId = new String[spotCount];
            spotImage = new String[spotCount];

            for (int i = 0 ; i<spotCount;i++) {

                latitude[i] = Double.parseDouble(resJson.getJSONArray("spotList").getJSONObject(i).getString("latitude"));
                longitude[i] = Double.parseDouble(resJson.getJSONArray("spotList").getJSONObject(i).getString("longitude"));
                spotName[i] = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotName");
                spotId[i] = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotId");
                spotImage[i] = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotImage");
            }


            // ListViewのインスタンスを生成
            ListView listView = activity.findViewById(R.id.spotList);

            // BaseAdapter を継承したadapterのインスタンスを生成
            // レイアウトファイル list_items.xml を
            // activity_main.xml に inflate するためにadapterに引数として渡す
            BaseAdapter adapter = new NearSpotListAdapter(activity.getApplicationContext(),
                    R.layout.inner_near_spot_list, spotName,spotImage,spotId);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public double getLatitude(int position){
        return latitude[position];
    }
    public double getLongitude(int position){
        return longitude[position];
    }
    public String getSpotId(int position){
        return spotId[position];
    }


}
