package com.example.trendpass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.trendpass.async.AsyncSetPositionActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import static com.example.trendpass.LocationService.serviceLatitude;
import static com.example.trendpass.LocationService.serviceLongitude;
import static com.example.trendpass.LocationService.servicelocationName;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

//メモ
/*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
 * ソート✨
 * 滞在開始時間と滞在終了時間を取得する
 * 滞在時間の取得
 */
public class DispMapActivity extends FragmentActivity implements OnMapReadyCallback {

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

    private FusedLocationProviderClient fusedLocationClient;


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


                    }
                });

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
            //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (LocationListener) this);
        }
        /*
        //接続
        try {
            String ip = getString(R.string.ip);

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


                                saveName = editText.getText().toString();

                                //現在地取得の設定
                                fusedLocationClient = LocationServices.getFusedLocationProviderClient(DispMapActivity.this);
                                @SuppressLint("RestrictedApi")
                                LocationRequest locationRequest = new LocationRequest();
                                locationRequest.setPriority(

                                        LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                                memo();
                                //複数回メモできるようにする処理
                                ViewGroup viewGroup = (ViewGroup) editText.getParent();
                                viewGroup.removeView(editText);


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
                                Intent intent = new Intent(DispMapActivity.this, NearBySpotsListActivity.class);
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
    private void checkMultiPermissions() {
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

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
    private void startLocationService() {
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
       // mLocationManager.removeUpdates(this.DispMapActivity);

    }

    //Activityが非表示のとき
    @Override
    public void onStop() {
        super.onStop();

        // Serviceの停止
        Intent intent = new Intent(getApplication(), LocationService.class);
        stopService(intent);

        System.out.println("位置情報の集計を一時停止");


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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

              //jsonファイルを読み込みファイルを読み込みマップのUIを変える
              try {
               // Customise the styling of the base map using a JSON object defined
               // in a raw resource file.
                boolean success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json));

                 if (!success) {
                   Log.e(TAG, "Style parsing failed.");
                 }
              } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "Can't find style. Error: ", e);
              }



        double latitude = serviceLatitude;
        double longitude = serviceLongitude;

        LatLng curr = new LatLng(latitude, longitude);
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(curr).zoom(15.0f)
                .bearing(0).build();

        // MyLocationButtonを有効に
        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);

        //マーカーのセット
        mMap.addMarker(new MarkerOptions().position(curr).title(servicelocationName).draggable(true).flat(true));


        //現在地表示ボタンを表示する
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
                    mMap.setMyLocationEnabled(true);
                    //渋滞表示(赤い線を表示する)
                    mMap.setTrafficEnabled(true);

                    // マップをタップした時のリスナーをセット
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                        @Override
                        public void onMapClick(LatLng tapLocation) {
                            // tapされた位置の緯度経度
                            LatLng location = new LatLng(tapLocation.latitude, tapLocation.longitude);

                            //タッチした場所の住所を検索する
                            String locationName = getLocationName(tapLocation.latitude,tapLocation.longitude);
                            //現在地からの距離を計算する
                            String fromDist = String.format(Locale.JAPAN,"現在地からの距離%skm",(calcDist(tapLocation.latitude,tapLocation.longitude) ) );

                            String str = String.format(Locale.JAPAN, "%s", locationName);

                            //マップをタッチすると青のピンを立てる
                            mMap.addMarker(new MarkerOptions().position(location).title(str).snippet(fromDist).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                           // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));

                            setMarker(tapLocation.latitude,tapLocation.longitude);

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
                            //他のピンを消す
                            mMap.clear();

                            //タッチした場所の住所を検索する
                            LatLng newlocation = new LatLng(longpushLocation.latitude,longpushLocation.longitude);

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 14));


                        }
                    });
                }

    //現在地からの距離を計算する
    private String calcDist(double latitude,double longitude){
        String fromDist = "";
        final double r = 6378.137; // 赤道半径[km]
        //距離の計算
        String distance = String.valueOf( acos(sin(serviceLatitude * PI / 180) * sin(latitude * PI / 180) + cos(serviceLatitude * PI / 180) * cos(latitude * PI / 180) * cos(longitude * PI / 180 - longitude * PI / 180) ) / 0.0001 );
        //インスタンスを生成
        BigDecimal bd = new BigDecimal(distance);
        //小数第２位以下切り捨て
        BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_DOWN);
        //doubleへキャスト
        fromDist = String.valueOf( bd2.doubleValue() );

        return fromDist;
    }



    //位置情報から住所を検索する
    private String getLocationName(double latitude,double longitude){
            String locationName = "";
        //緯度・経度から住所を取得する
        Geocoder geocoder = new Geocoder(this, Locale.JAPAN);
        List<Address> addresses = null;
        try {
            StringBuffer strBuf = new StringBuffer();
            addresses = geocoder.getFromLocation(latitude,longitude, 1);

            //逆ジオコーディング
            if (!addresses.isEmpty()) {

                strBuf.append(addresses.get(0).getAdminArea());   //都市名取得

                if(addresses.get(0).getSubAdminArea()!=null) {
                    strBuf.append(addresses.get(0).getSubAdminArea());//郡にあたる場所
                }
                if( addresses.get(0).getLocality() != null) {
                    strBuf.append(addresses.get(0).getLocality());    //市区町村取得
                }
                if(addresses.get(0).getThoroughfare() != null) {
                    strBuf.append(addresses.get(0).getThoroughfare());//〇〇丁目
                }
                if(addresses.get(0).getSubThoroughfare() != null) {
                    strBuf.append(addresses.get(0).getSubThoroughfare() + "番");//〇〇番
                }
                if(addresses.get(0).getFeatureName() != null) {
                    strBuf.append(addresses.get(0).getFeatureName());    //〇〇号
                }
                locationName = strBuf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationName;
    }

    @SuppressWarnings("MissingPermission")
    private void memo() {

        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    // ログイン情報破棄
                                    SharedPreferences oldData = getSharedPreferences("DataStore", MODE_PRIVATE);

                                    if(oldData!=null){
                                        SharedPreferences.Editor editor = oldData.edit();

                                        editor.clear();
                                    }
                                        Location location = task.getResult();

                                        double latitude =  location.getLatitude();
                                        double longitude = location.getLongitude();

                                        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                        final Date date = new Date(System.currentTimeMillis());
                                        String getTime = df.format(date);

                                        System.out.println("saveLocationメソッド："+ saveName);

                                        //ファイル名を保存名にする
                                        SharedPreferences data = getSharedPreferences("saveName", MODE_PRIVATE);
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

                                } else {
                                    Log.d("debug","計測不能");
                                    System.out.println("計測不能");
                                }
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

                            new AsyncSetPositionActivity(DispMapActivity.this)
                                    .execute(new URL("http://" + ip + ":8080/trendpass/SetPositionServlet?latitude="+latitude
                                            + "&longitude="+longitude+"&userId="+userId+"&stayStart="+getTime ) ) ;

                        } catch (MalformedURLException e) {
                            e.printStackTrace();

                        }


                    }

                }
            }


