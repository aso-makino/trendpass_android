package com.example.trendpass;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.example.trendpass.DispMapActivity.running;
import static com.example.trendpass.DispMapActivity.saveName;
import static com.example.trendpass.DispMapActivity.userId;

/*
 *メモ
 * 位置情報の保存が1回しかできない（2回目以降保存ボタンを押すとエラー）
 *
 * ///////////////////////////////////重要//////////////////////////////////////////////////
 * どこからのクラスでこのクラスのサービスを明示的に止める処理が必要
 */
public class LocationService extends IntentService implements LocationListener {

    private LocationManager locationManager;
    private Context context;
    private TextView textView;
    private StringBuffer strBuf = new StringBuffer();
    private static final int MinTime = 1000;
    private static final float MinDistance = 50;

    private static final String TAG = "LocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000; //10秒
    private static final float LOCATION_DISTANCE = 10f; //10m

    public static double serviceLatitude = 0.0;
    public static double serviceLongitude = 0.0;
    public static String servicelocationName;
    public LocationService(String location) {
        super(location);
        // TODO 自動生成されたコンストラクター・スタブ
    }
    public LocationService(){
        super("IntentService");
    }

    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int requestCode = 0;
        String channelId = "default";
        String title = context.getString(R.string.app_name);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, requestCode,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // ForegroundにするためNotificationが必要、Contextを設定
        NotificationManager notificationManager =
                (NotificationManager)context.
                        getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification　Channel 設定
        NotificationChannel channel = new NotificationChannel(
                channelId, title , NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Silent Notification");
        // 通知音を消さないと毎回通知音が出てしまう
        // この辺りの設定はcleanにしてから変更
        channel.setSound(null,null);
        // 通知ランプを消す
        channel.enableLights(false);
        channel.setLightColor(Color.BLUE);
        // 通知バイブレーション無し
        channel.enableVibration(false);

        //---use the LocationManager class to obtain locations data---
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent i = new Intent(this, LocationService.class);

        //---request for location updates using GPS---
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    pendingIntent);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
        if(notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    // 本来なら衛星のアイコンですがandroid標準アイコンを設定
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setContentText("GPS")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build();

            // startForeground
            startForeground(1, notification);
        }

        startGPS();
    }
    //位置情報の集計を開始
    protected void startGPS() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("startGPS\n");

        final boolean gpsEnabled
                = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            enableLocationSettings();
        }

        if (locationManager != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MinTime, MinDistance, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            strBuf.append("locationManager=null\n");
        }
    }

    /*
    * @param latitude 緯度
    * @param longitude 経度
    * @param getTime 取得時間
    * @param strBuf 地名
    * @param userId ユーザーID
    * @param saveName 保存名
    * @param runnning GPSが起動しているか
    */
    @Override
    public void onLocationChanged(Location location) {


        int count = 1;
        //1分に位置情報を取得し送信する
        //スポット滞在判定
        try {

           while(running) {
               //緯度・経度取得
               serviceLatitude  = location.getLatitude();
               serviceLongitude = location.getLongitude();
               //位置情報を一時的に記憶
               LatLng stayLocation = new LatLng(serviceLatitude,serviceLongitude);

               //時間計測
               SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.JAPAN);
               String getTime = sdf.format(location.getTime());


               //緯度・経度から住所を取得する
               Geocoder geocoder = new Geocoder(this, Locale.JAPAN);
               List<Address> addresses = null;
               try {
                   strBuf = new StringBuffer();
                   addresses = geocoder.getFromLocation(serviceLatitude,serviceLongitude, 1);

                   //逆ジオコーディング
                   if (!addresses.isEmpty()) {

                       strBuf.append(addresses.get(0).getAdminArea());   //都市名取得

                       if(addresses.get(0).getSubAdminArea()!=null) {
                           strBuf.append(addresses.get(0).getSubAdminArea());//郡にあたる場所
                       }


                       strBuf.append(addresses.get(0).getLocality());    //市区町村取得
                       strBuf.append(addresses.get(0).getThoroughfare());    //〇〇丁目
                       strBuf.append(addresses.get(0).getSubThoroughfare()+"番");    //〇〇番
                       strBuf.append(addresses.get(0).getFeatureName());    //〇〇号

                       servicelocationName = strBuf.toString();

                       //位置情報をDispMapActivityに送る
                       sendLocation(servicelocationName,  serviceLatitude, serviceLongitude, getTime, userId);
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
               //ユーザーが保存を選択している場合は位置情報をxmlファイルに書き込む
               if(saveName != null) {
                   saveLocation(servicelocationName, serviceLatitude, serviceLongitude, getTime, userId, saveName);
                   saveName = null;
               }
               //ログを吐く
               Log.d("debug", "sleep: " + String.valueOf(count));

               count++;
                   //60秒間停止する
                   Thread.sleep(60000);
               }

        } catch (InterruptedException e) {
            onDestroy();
            Thread.currentThread().interrupt();
        }
    }

    public String getLocationName(double latitude,double longitude) {
        String returnLocationName;

        //error//
        Geocoder geocoder = new Geocoder(this, Locale.JAPAN );
        List<Address> addresses = null;
        try {
             returnLocationName = "";

            strBuf = new StringBuffer();
            addresses = geocoder.getFromLocation(latitude,longitude, 1);

            //逆ジオコーディング
            if (!addresses.isEmpty()) {

                strBuf.append(addresses.get(0).getAdminArea());   //都市名取得

                if(addresses.get(0).getSubAdminArea()!=null) {
                    strBuf.append(addresses.get(0).getSubAdminArea());//郡にあたる場所
                }

                strBuf.append(addresses.get(0).getLocality());    //市区町村取得
                strBuf.append(addresses.get(0).getThoroughfare());    //〇〇丁目
                strBuf.append(addresses.get(0).getSubThoroughfare()+"番");    //〇〇番
                strBuf.append(addresses.get(0).getFeatureName());    //〇〇号

                returnLocationName = strBuf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
            return null;
        }
        return returnLocationName;
    }




    /*
     * DispMapActivityへ値を渡す
     * @param StrBuf 地名
     * @param latitude 緯度
     * @param longitude 経度
     * @param getTime 取得時
     * @param userId ユーザーID
     */
    private void sendLocation(String locationName,double latitude,double longitude,String getTime,String userId){
        Intent broadcast = new Intent();
        broadcast.putExtra("location", locationName) ;
        broadcast.putExtra("latitude", latitude);
        broadcast.putExtra("longitude", longitude);
        broadcast.putExtra("getTime", getTime);
        broadcast.putExtra("userId", userId);
        broadcast.setAction("DO_ACTION");
        getBaseContext().sendBroadcast(broadcast);
    }

    /*
     * 位置情報データを保存する
     * @param StrBuf 地名
     * @param latitude 緯度
     * @param longitude 経度
     * @param getTime 取得時
     * @param userId ユーザーID
     */
    private void saveLocation(String servicelocationName, double latitude, double longitude, String getTime, String userId, String saveName) {

        System.out.println("saveLocationメソッド："+ saveName);

        SharedPreferences data = getSharedPreferences(saveName, MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        //キーと値を保存する
        editor.putString("saveName",saveName );
        editor.putString("location",servicelocationName );
        editor.putString("latitude", String.valueOf(latitude));
        editor.putString("longitude",String.valueOf(longitude));
        editor.putString("getTime",getTime);
        editor.putString("userId",userId);
        // 書き込みを確定する
        editor.commit();
    }

    /*
     * 位置情報保存ファイルを削除する
     * @param StrBuf 地名
     * @param latitude 緯度
     * @param longitude 経度
     * @param getTime 取得時
     * @param userId ユーザーID
     */
    private void ClearLocation(String locationName,double latitude,double longitude,String getTime,String userId) {

        SharedPreferences data = getSharedPreferences(saveName, MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        //削除
        editor.clear();
        editor.commit();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                strBuf.append("LocationProvider.AVAILABLE\n");
                textView.setText(strBuf);
                break;
            case LocationProvider.OUT_OF_SERVICE:
                strBuf.append("LocationProvider.OUT_OF_SERVICE\n");
                textView.setText(strBuf);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                strBuf.append("LocationProvider.TEMPORARILY_UNAVAILABLE\n");
                textView.setText(strBuf);
                break;
        }

    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
    //位置情報の取得を停止
    private void stopGPS(){
        if (locationManager != null) {

            System.out.println("STOP:GPSメソッド：位置情報の集計を停止します");
            // update を止める
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }

            stopSelf();
            locationManager.removeUpdates(this);
        }
    }
//終了時に呼ばれる
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGPS();
    }
}

