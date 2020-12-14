package com.example.trendpass;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.trendpass.async.AsyncSetPositionActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import static com.example.trendpass.LocationService.serviceLatitude;
import static com.example.trendpass.LocationService.serviceLongitude;
import static com.example.trendpass.LocationService.servicelocationName;

//メモ
/*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
 * ソート✨
 * 滞在開始時間と滞在終了時間を取得する
 * 滞在時間の取得
 */
public class DispMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = DispMapActivity.class.getSimpleName();
    private static final int REQUEST_MULTI_PERMISSIONS = 1001;
    private static GoogleMap mMap;
    private Marker marker;
    private LatLng latlng;
    private LocationManager mLocationManager;
    private String bestProvider;
    private SupportMapFragment mapFragment;

    //レイアウトの部品
    public static boolean running = false;
    public static String spotName;
    public static String saveName = null;
    public static String userId;

    private double dispMapLatitude = serviceLatitude;
    private double dispMapLongitude = serviceLongitude;
    //ソート項目
    private boolean isPopular = false;
    private String genre;
    private String sex;
    private double minDist = 0.0;
    private double maxDist = 0.0;
    private int generation = 0;
    //タイマー処理用
    private Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        userId = loginData.getString("userId", "");


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //マップの形成
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        // receiver（サービスから値を取得する際使うやつ）
        UpdateReceiver receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("DO_ACTION");
        registerReceiver(receiver, filter);

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //マルチスレッドにする処理
                // Android 6, API 23以上でパーミッシンの確認
                if (Build.VERSION.SDK_INT >= 23) {
                    checkMultiPermissions();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        //位置情報を取得するサービスを起動する
                      //  startLocationService();
                    }
                });
                //マルチスレッド終わり

            }
        }).start();

        initLocationManager();
    }
    
    @Override
    public void onResume() {
        super.onResume();



        // isProviderEnabledはWi-Fiから位置情報を取得できるか確認するメソッド
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
        /*
        //接続
        try {
            String ip = getString(R.string.ip);
            System .out .println();
            new AsyncSpotListActivity(DispMapActivity.this)
                    .execute(new URL("http://" + ip + ":8080/trendpass/SpotListServlet?latitude="+serviceLatitude
                            + "&longitude="+serviceLongitude+"&userId="+userId));

        } catch (MalformedURLException e) {
            e.printStackTrace();

        }

         */


        final EditText editText = new EditText(this);
        //位置情報保存ボタンを作成
        ImageButton saveGPSButton = findViewById(R.id.saveGPS);
        saveGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //保存ダイアログを表示
                new AlertDialog.Builder(DispMapActivity.this)
                        .setTitle("位置情報を保存しますか")
                        .setMessage("保存名")
                        .setView(editText)
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //テスト用
                                SharedPreferences restore_info_prefs = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                                SharedPreferences.Editor restore_info = restore_info_prefs.edit();
                                //まずはxmlの内部データ数を取得
                                int count = restore_info_prefs.getInt("count", 0);
                                // テスト用にデータを挿入
                                //Sony 702SO Android9,API28,com.example.trendpass.shared_pref
                                restore_info.putInt("count", count += 1);
                                restore_info.putString("address"+count, saveName);
                                restore_info.putString("date"+count, "2020/12/10");
                                restore_info.putString("memo"+count, "てすと");
                                restore_info.putString("latitude"+count, String.format("%.4f", dispMapLatitude));
                                System.out.println("てすと" + dispMapLatitude);
                                restore_info.putString("longitude"+count, String.format("%.4f", dispMapLongitude));
                                System.out.println("てすと" + dispMapLongitude);

                                restore_info.apply();
                                restore_info.commit();
                                if (editText.length() > 0) {
                                    editText.getText().clear();
                                }
                            }

                        })

                        // いいえボタンの処理
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //　何もしない
                                return;
                            }
                        })

                        .create()
                        .show();
            }
        });

        //footerの生成
        ImageButton mapButton = findViewById(R.id.mapbtn);
        ImageButton insertButton = findViewById(R.id.insertbtn);
        ImageButton mapListButton = findViewById(R.id.listbtn);
        ImageButton userButton = findViewById(R.id.userbtn);





        //ユーザーボタンをタッチした時の処理
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチしてユーザー設定画面へ
            public void onClick(View view) {
                //マイページ画面へ
                Intent intent = new Intent(DispMapActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(DispMapActivity.this)
                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //位置情報履歴画面へ
                                Intent intent = new Intent(DispMapActivity.this,NearBySpotsListActivity.class);
                                startActivity(intent);

                            }
                        })
                        // 口コミ投稿ボタンの処理
                        .setNegativeButton("口コミ投稿", new DialogInterface.OnClickListener() {
                            @Override
                            //口コミ投稿ボタンをタッチ
                            public void onClick(DialogInterface dialog, int which) {
                                ////////////////////////////
                                //口コミ投稿画面へ
                                Intent intent = new Intent(DispMapActivity.this, NearSpotListActivity.class);
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        });


        //スポットリストボタンをタッチした時の処理
        mapListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチスポット一覧表示画面へ遷移する
            public void onClick(View view) {
                ///////////////////////////
                //現在地周辺スポット一覧画面へ
                Intent intent = new Intent(DispMapActivity.this, DispSpotListActivity.class);
                startActivity(intent);
                Log.v("Alert", "スポット一覧へ");
            }
        });

        //マップボタンをタッチした時の処理
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチしてユーザー設定画面へ
            public void onClick(View view) {
                //設定画面へ
                Intent intent = new Intent(DispMapActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });

        }

    // 位置情報許可の確認、外部ストレージのPermissionにも対応できるようにしておく
    private void checkMultiPermissions () {
        // 位置情報の Permission
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        // 外部ストレージ書き込みの Permission
        int permissionExtStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ArrayList reqPermissions = new ArrayList<>();

        // 位置情報の Permission が許可されているか確認
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            // 許可済
        } else {
            // 未許可
            reqPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 外部ストレージ書き込みが許可されているか確認
        if (permissionExtStorage == PackageManager.PERMISSION_GRANTED) {
            // 許可済
        } else {
            // 許可をリクエスト
            reqPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // 未許可
        if (!reqPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    (String[]) reqPermissions.toArray(new String[0]),
                    REQUEST_MULTI_PERMISSIONS);
            running = false;
            // 未許可あり
        } else {
            // 許可済
            running = true;
            startLocationService();
        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult ( int requestCode,
                                             @NonNull String[] permissions, @NonNull int[] grantResults){

        if (requestCode == REQUEST_MULTI_PERMISSIONS) {
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    // 位置情報
                    if (permissions[i].
                            equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // 許可された

                        } else {
                            // それでも拒否された時の対応
                        }
                    }
                    // 外部ストレージ
                    else if (permissions[i].
                            equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // 許可された
                        } else {
                            // それでも拒否された時の対応
                        }
                    }
                }
                startLocationService();

            }
        }
    }

    // LocationServiceに遷移する
    private void startLocationService () {
        Intent intent = new Intent(DispMapActivity.this, LocationService.class);
        // API 26 以降
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }

    }

    private void initLocationManager() {
        // インスタンス生成
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 詳細設定
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        bestProvider = mLocationManager.getBestProvider(criteria, true);
    }

    //Activityの終了
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Serviceの停止
        Intent intent = new Intent(getApplication(), LocationService.class);
        stopService(intent);
    }

    //他のActivityが実行中
    @Override
    public void onPause() {
        super.onPause();
        // 電池の節約のために、端末がスリープしたら位置情報取得処理を止める
        mLocationManager.removeUpdates(this);

    }
    //Activityが非表示のとき
    @Override
    public void onStop() {
        super.onStop();

        // Serviceの停止
        Intent intent = new Intent(getApplication(), LocationService.class);
        stopService(intent);

        System.out.println("onStopメソッド：位置情報の集計を一時停止します");


    }
    //OnStopの後、再び呼び出されたとき
    @Override
    public void onRestart() {
        super.onRestart();

    }
    //OnRestartの後の処理
    @Override
    public void onStart() {
        super.onStart();

    }

    /*位置情報が更新された場合に呼び出される*/
                @Override
                public void onLocationChanged (Location location){


                    //緯度
                    dispMapLatitude = location.getLatitude();
                    //経度
                    dispMapLongitude = location.getLongitude();



                    //緯度・経度取得
                    dispMapLatitude  =( Math.floor( (location.getLatitude() * 10000000) ) / 10000000 );
                    dispMapLongitude = ( Math.floor( (location.getLongitude() * 10000000) ) / 10000000 );





                    latlng = new LatLng(dispMapLatitude, dispMapLongitude);
                    //確認
                    System.out.println(latlng);

                    CameraPosition cameraPos = new CameraPosition.Builder()
                            .target(latlng).zoom(15.0f)
                            .bearing(0).tilt(60).build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));

                    CameraUpdate update = CameraUpdateFactory.newLatLng(latlng);
                    // 表示位置を地図に反映させる。
                    mMap.moveCamera(update);
                }

                @Override
                public void onStatusChanged (String provider,int status, Bundle extras){

                }

                @Override
                public void onProviderEnabled (String provider){

                }

                @Override
                public void onProviderDisabled (String provider){

                }

                @Override
                public void onMapReady (GoogleMap googleMap){
                    mMap = googleMap;
                    /*
                    //jsonファイルを読み込みファイルを読み込みマップのUIを変える
                    try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = googleMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        this, R.raw.style_json));

                        if (!success) {
                            Log.e(TAG, "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "Can't find style. Error: ", e);
                    }
                    */
                    // MyLocationレイヤーを有効に
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (mMap != null) {
                        // 現在地更新
                        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                            @Override
                            public void onMyLocationChange(Location location) {


                                double latitude  =  serviceLatitude;
                                double longitude =  serviceLongitude;

                                LatLng curr = new LatLng(latitude, longitude);

                                mMap.animateCamera(CameraUpdateFactory.newLatLng(curr));

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
                                // camera 移動
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 10));
                                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(latitude, longitude), 12);

                                mMap.moveCamera(cUpdate);

                                mMap.getUiSettings().setZoomControlsEnabled(true);

                                CameraPosition cameraPos = new CameraPosition.Builder()
                                        .target(curr).zoom(12.0f)
                                        .bearing(0).build();

                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));


                                // MyLocationButtonを有効に
                                UiSettings settings = mMap.getUiSettings();
                                settings.setMyLocationButtonEnabled(true);

                                setMarker(latitude, longitude);
                                // アイコン画像をマーカーに設定
                                setIcon(latitude, longitude);

                                //マーカーのセット
                                mMap.addMarker(new MarkerOptions().position(curr).title(servicelocationName).draggable(true).flat(true));
                            }
                        });
                    }

                    //現在地表示ボタンを表示する
                    mMap.setMyLocationEnabled(true);
                    //渋滞表示(赤い線を表示する)
                    mMap.setTrafficEnabled(true);

                    // マップをタップした時のリスナーをセット
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                        @Override
                        public void onMapClick(LatLng tapLocation) {
                            //位置情報が取れている
                            // tapされた位置の緯度経度
                            latlng = new LatLng(dispMapLatitude, dispMapLongitude);


                            String str = String.format(Locale.JAPAN, "%s", "タッチしたばしょ");


                            //マップにマーカーをセットする
                            //.iconでマップをタッチしてアイコン表示する
                            mMap.addMarker(new MarkerOptions().position(latlng).title(str).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            );
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));


                            LatLng location = new LatLng(dispMapLatitude, dispMapLongitude);


                            setMarker(dispMapLatitude, dispMapLongitude);

                            MarkerOptions options = new MarkerOptions();
                            options.position(location);
                            // マップにマーカー追加
                            Marker marker = mMap.addMarker(options);

                        }
                    });

                    // 長押しのリスナーをセット
                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng longpushLocation) {
                            String getLocationName;
                            LocationService ls = new LocationService();
                            //他のピンを消す
                            mMap.clear();

                           // getLocationName = ls.getLocationName(dispMapLatitude,dispMapLongitude);

                            LatLng newlocation = new LatLng(dispMapLatitude,dispMapLongitude);
                            //現在地の座標を表示する。地名を出したい
                            mMap.addMarker(new MarkerOptions().position(newlocation).title("長押し")
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 14));


                        }
                    });
                }

    //マッカーのセット
                private void setMarker ( double latitude, double longitude){
                    latlng = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latlng);
                    markerOptions.title(servicelocationName);
                    mMap.addMarker(markerOptions);
                }

                private void setIcon ( double latitude, double longitude){
                    latlng = new LatLng(latitude, longitude);

                    //画像の読み込み
                    BitmapDescriptor descriptor =
                            BitmapDescriptorFactory.fromResource(R.drawable.pin);

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

                /*
                 *サービスから位置情報を受けとりServletへ送信する
                 * @param getLoacationName 地名
                 * @param latitude 緯度
                 * @param longitude 経度
                 * @param getTime 取得時
                 * */

              final class UpdateReceiver extends BroadcastReceiver {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        Bundle extras    = intent.getExtras();
                        double latitude  = extras.getDouble("latitude");
                        double longitude = extras.getDouble("longitude");

                        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                        final Date date = new Date(System.currentTimeMillis());
                        String getTime = df.format(date);
                        //接続
                        try {
                            String ip = getString(R.string.ip);

                            System.out.println("http://" + ip + ":8080/trendpass/SetPositionServlet?latitude="+latitude
                                    + "&longitude="+longitude+"&userId="+userId+"&stayStart="+getTime);

                            new AsyncSetPositionActivity(DispMapActivity.this)
                                    .execute(new URL("http://" + ip + ":8080/trendpass/SetPositionServlet?latitude="+latitude
                                            + "&longitude="+longitude+"&userId="+userId+"&stayStart="+getTime ) ) ;

                        } catch (MalformedURLException e) {
                            e.printStackTrace();

                        }


                    }



                }
            }


