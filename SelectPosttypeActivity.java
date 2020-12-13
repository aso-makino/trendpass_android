package com.example.trendpass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SelectPosttypeActivity extends AppCompatActivity implements LocationListener {

    /*変数宣言*/
    private LocationManager manager;
    private Location location;
    private String spot_name,latitude_str,longitude_str;
    //緯度経度
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_posttype);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
    /*
     * onResumeメソッド
     * */

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        /*位置情報を取得するメソッド*/
        //引数:プロバイダ（GPSやNETWORK）、最小時間間隔（ミリ秒）、最小距離間隔（メートル）
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 10f, this);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000, 10f, this);

        /*現在の位置情報ボタン*/
        Button currentBtn = (Button) findViewById(R.id.current_button);
        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*遷移*/
                Intent intent = new Intent(SelectPosttypeActivity.this, InsertSpotActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });

        /*位置情報リストボタン*/
        Button memospotBtn = (Button) findViewById(R.id.memo_spot_button);
        memospotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //内部データから情報を全て取得
                SharedPreferences restore_info_prefs = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                /*
                Map<String,?> map_name = restore_info_prefs.getAll();
                String key_name;
                Object value_name;

                ArrayList<String> geoLocationList = new ArrayList<>();
                ArrayList<String> dataList = new ArrayList<>();
                ArrayList<String> memoList = new ArrayList<>();
                ArrayList<String> longitudeList = new ArrayList<>();
                ArrayList<String> latitudeList = new ArrayList<>();

                geoLocationList.add(restore_info_prefs.getString("address","失敗"));
                scenes = geoLocationList.toArray(new String[0]);
                dataList.add(restore_info_prefs.getString("date","1970/1/1"));
                datetexts = dataList.toArray(new String[0]);
                memoList.add(restore_info_prefs.getString("memo","失敗"));
                memos = memoList.toArray(new String[0]);
                longitudeList.add(restore_info_prefs.getString("longitude","0.0"));
                longitude = longitudeList.toArray(new String[0]);
                latitudeList.add(restore_info_prefs.getString("latitude","0.0"));
                latitude = latitudeList.toArray(new String[0]);
                 */
                //DeviceFileExplorerのcom.example.trendpassのshared_prefsの中のxmlに追加される
                spot_name = restore_info_prefs.getString("address","読み取りに失敗");
                latitude_str = restore_info_prefs.getString("latitude","0.0");
                longitude_str = restore_info_prefs.getString("longitude","0.0");

                /*遷移*/
                Intent intent = new Intent(SelectPosttypeActivity.this, NearBySpotsListActivity.class);

                startActivity(intent);
            }
        });

        /*口コミ投稿ボタン*/
        /*
        Button reviewBtn = (Button) findViewById(R.id.review_button);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
                /*遷移*/
                /*Intent intent = new Intent(SelectPosttypeActivity.this, InsertReviewActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    /*新たなアクティビティ（画面）が開始して、現在のアクティビティ（画面）が表示されなくなったときに呼ばれます。*/
    protected void onStop() {
        super.onStop();

        if (manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //位置情報の取得処理を終了
            manager.removeUpdates(this);
        }
    }

    @Override
    /*プロバイダのステータスが変更されたとき呼ばれる*/

    public void onLocationChanged(Location location) {
        //StringBufferはマルチスレッドで、同じStringBufferのインスタンスにアクセスできます。
        StringBuffer strBuf = new StringBuffer();
        //緯度を取得
        final double latitude = location.getLatitude();
        //経度を取得
        final double longitude = location.getLongitude();
        //テスト用
        System.out.println(latitude);
        System.out.println(longitude);
        //本日の日付を取得
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.JAPAN);
        String getTime = sdf.format(location.getTime());
        //緯度・経度から住所を取得する
        Geocoder geocoder = new Geocoder(this, Locale.JAPAN);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            //逆ジオコーディング
            if (!addresses.isEmpty()) {
                strBuf.append("〒" + addresses.get(0).getPostalCode() + "\n");//郵便番号
                strBuf.append(addresses.get(0).getAdminArea());   //都市名取得

                if (addresses.get(0).getSubAdminArea() != null) {
                    strBuf.append(addresses.get(0).getSubAdminArea());//郡にあたる場所
                }
                strBuf.append(addresses.get(0).getLocality());    //市区町村取得
                strBuf.append(addresses.get(0).getThoroughfare());    //〇〇丁目
                strBuf.append(addresses.get(0).getSubThoroughfare() + "番");    //〇〇番
                strBuf.append(addresses.get(0).getFeatureName());    //〇〇号
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //テスト用　取得した文字列を表示
        System.out.println(strBuf);
        //現在地情報ボタンのラベルを変更
        Button geolocationBtn = (Button) findViewById(R.id.current_button);
        geolocationBtn.setText("現在地は\n" + strBuf);
    }

    /*implements LocationListener　に必要な3つのメソッド*/
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
