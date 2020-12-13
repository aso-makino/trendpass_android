package com.example.trendpass;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncNearSpotListActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.net.MalformedURLException;
import java.net.URL;

public class NearSpotListActivity extends AppCompatActivity {
    AsyncNearSpotListActivity asyncNearSpotListActivity;

    //現在地取得のための変数宣言
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;

    private String SpotText;
    private String SpotLatitude;
    private String SpotLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_spot_list);

        //位置情報履歴から取得
        Intent i = getIntent();
        //保存されたスポット名
        SpotText = i.getStringExtra("SpotText");
        SpotLatitude = i.getStringExtra("SpotLatitude");
        SpotLongitude = i.getStringExtra("SpotLongitude");


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        @SuppressLint("RestrictedApi")
        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setPriority(
// どれにするかはお好みで、ただしできない状況ではできないので
//                LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//                LocationRequest.PRIORITY_LOW_POWER);
//                LocationRequest.PRIORITY_NO_POWER);


        /////////////////////////////////////////////////////////
        getLastLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();



        Button insertSpotBtn = (Button)findViewById(R.id.InsertSpotBtn);
        insertSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NearSpotListActivity.this, InsertSpotActivity.class);
                intent.putExtra("SpotText", SpotText);
                intent.putExtra("SpotLatitude", SpotLatitude);
                intent.putExtra("SpotLongitude", SpotLongitude);

                startActivity(intent);
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
                Intent intent = new Intent(NearSpotListActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(NearSpotListActivity.this)
                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //位置情報履歴画面へ
                                Intent intent = new Intent(NearSpotListActivity.this,NearBySpotsListActivity.class);
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
                                Intent intent = new Intent(NearSpotListActivity.this, NearSpotListActivity.class);
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
                Intent intent = new Intent(NearSpotListActivity.this, DispSpotListActivity.class);
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
                Intent intent = new Intent(NearSpotListActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });


    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    location = task.getResult();

                                    latitude =  location.getLatitude();
                                    longitude = location.getLongitude();


                                    System.out.println("おっけー");
                                    System.out.println(longitude + "//" + latitude);
                                } else {
                                    Log.d("debug","計測不能");
                                    System.out.println("計測不能");
                                }

                                try {
                                    String ip= getString(R.string.ip);
                                    AsyncNearSpotListActivity asyncNearSpotListActivity = new AsyncNearSpotListActivity(NearSpotListActivity.this);
                                    System.out.println("http://"+ip+":8080/trendpass/NearSpotList?latitude="+latitude+"&longitude="+longitude);
                                    asyncNearSpotListActivity.execute(new URL("http://"+ip+":8080/trendpass/NearSpotList?latitude="+latitude+"&longitude="+longitude));


                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }
}
