package com.example.trendpass.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.GridView;

import com.example.trendpass.GridAdapter;
import com.example.trendpass.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;

public class AsyncSpotListActivity extends AsyncBaseActivity {

    private String url;
    GoogleMap mMap;


    public AsyncSpotListActivity(Activity activity) {
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

        double latitude = 0.0;
        double longitude = 0.0;

        String spotId = "";
        String spotName = "";
        String genreId = "";
        String fromDist = "";
        String spotImage = "";

        //　スポットの情報
        try {

            int spotListSize = resJson.getInt("spotSize");

            String[] image = new String[spotListSize];

            for(int i = 0; i < spotListSize; i++){

                HashMap<String,Object> spotListMap = new HashMap<String,Object>();

                spotId = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotId");
                spotName = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotName");
                latitude  = Double.parseDouble( resJson.getJSONArray("spotList").getJSONObject(i).getString("latitude") ) ;
                longitude = Double.parseDouble( resJson.getJSONArray("spotBeansList").getJSONObject(i).getString("longitude") );
                genreId = resJson.getJSONArray("spotList").getJSONObject(i).getString("genreId");
                spotImage = resJson.getJSONArray("spotList").getJSONObject(i).getString("spotImage");
                fromDist = resJson.getJSONArray("spotList").getJSONObject(i).getString("fromDist");



                System.out.println(spotId+spotName+genreId+fromDist+spotImage);

                spotListMap.put("spotId",spotId);
                spotListMap.put("spotName",spotName);
                spotListMap.put("latitude",latitude);
                spotListMap.put("longitude",longitude);
                spotListMap.put("genreId",genreId);
                spotListMap.put("spotImage",spotImage);
                spotListMap.put("fromDist",fromDist);

                image[i] = spotImage;


                //スポット写真をマップに表示する
                setIcon(latitude,longitude,image);
            }

        // GridViewのインスタンスを生成
        GridView gridview = activity.findViewById(R.id.gridview);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル activity_maps.xml を
        // activity_maps.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(
                activity.getApplicationContext(),
                R.layout.activity_maps,
                image
        );


            // gridViewにadapterをセット
            gridview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            e.getMessage();
        }

}

    //写真をセットする
    private void setIcon(double latitude, double longitude, String[] image) {

        LatLng latlng = new LatLng(latitude,longitude);

        //画像の読み込み
        BitmapDescriptor descriptor =
                BitmapDescriptorFactory.fromResource(R.id.image);

        // 貼り付設定
        GroundOverlayOptions overlayOptions = new GroundOverlayOptions();
        overlayOptions.image(descriptor);

        overlayOptions.anchor(0.5f, 0.5f);

        overlayOptions.position(latlng, 300f, 300f);

        // マップに貼り付け・アルファを設定
        GroundOverlay overlay = mMap.addGroundOverlay(overlayOptions);
        // 透明度
        overlay.setTransparency(0.0F);
    }

}


