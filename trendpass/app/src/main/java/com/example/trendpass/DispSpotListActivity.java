package com.example.trendpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.trendpass.async.AsyncDispRankingActivity;
import com.example.trendpass.async.AsyncDispSpotListActivity;
import com.example.trendpass.async.AsyncMyPageReviewActivity;
import com.example.trendpass.async.AsyncNearSpotListActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.net.MalformedURLException;
import java.net.URL;

public class DispSpotListActivity extends AppCompatActivity {

    //現在地取得のための変数宣言
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_spot_list);

        //タブの下線を左にずらす
        View tabBar = findViewById(R.id.tabBar);
        animateTranslationStart(tabBar);

        //ランキングボタンを押せないようにする
        final Button rankingBtn = findViewById(R.id.dispRankingBtn);
        rankingBtn.setEnabled(false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(DispSpotListActivity.this);

        @SuppressLint("RestrictedApi")
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        getRanking();
    }


    @Override
    protected void onResume() {
        super.onResume();

        final Button rankingBtn = findViewById(R.id.dispRankingBtn);
        final Button dispPassBtn = findViewById(R.id.dispPassBtn);

        rankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //タブの下線切り替え
                View tabBar = findViewById(R.id.tabBar);
                animateTranslationXRight(tabBar);

                //ランキングボタンを押せなくし、色を青に
                rankingBtn.setEnabled(false);
                rankingBtn.setTextColor(Color.parseColor("#2196F3"));

                //すれちがいボタンを押せるようにし、色を灰色に
                dispPassBtn.setEnabled(true);
                dispPassBtn.setTextColor(Color.parseColor("#5E5E5E"));

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(DispSpotListActivity.this);

                @SuppressLint("RestrictedApi")
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setPriority(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                getRanking();
            }
        });

        dispPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //タブの下線切り替え
                    View tabBar = findViewById(R.id.tabBar);
                    animateTranslationXLeft(tabBar);

                    //すれちがいボタンを押せなくし、色を青に
                    dispPassBtn.setEnabled(false);
                    dispPassBtn.setTextColor(Color.parseColor("#2196F3"));

                    //ランキングボタンを押せるようにし、色を灰色に
                    rankingBtn.setEnabled(true);
                    rankingBtn.setTextColor(Color.parseColor("#5E5E5E"));

                    //　ユーザーIDを取得
                    SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
                    String userId = loginData.getString("userId", "");

                    String ip= getString(R.string.ip);
                    new AsyncDispSpotListActivity(DispSpotListActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/SpotListServlet?userId=" + userId));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
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
                Intent intent = new Intent(DispSpotListActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(DispSpotListActivity.this)
                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //位置情報履歴画面へ
                                Intent intent = new Intent(DispSpotListActivity.this,NearBySpotsListActivity.class);
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
                                Intent intent = new Intent(DispSpotListActivity.this, NearSpotListActivity.class);
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
                Intent intent = new Intent(DispSpotListActivity.this, DispSpotListActivity.class);
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
                Intent intent = new Intent(DispSpotListActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });


    }
    @SuppressWarnings("MissingPermission")
    private void getRanking() {
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
                                    new AsyncDispRankingActivity(DispSpotListActivity.this)
                                            .execute(new URL("http://"+ip+":8080/trendpass/RankingServlet?latitude=" + latitude + "&longitude=" + longitude));

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }
    private void animateTranslationStart( View target ) {

        // translationXプロパティを0fから200fに変化させます
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat( target, "translationX", 0f, -542 );

        // 3秒かけて実行させます
        objectAnimator.setDuration( 0 );

        // アニメーションを開始します
        objectAnimator.start();
    }
    private void animateTranslationXRight( View target ) {

        // translationXプロパティを0fから200fに変化させます
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat( target, "translationX", 0f, -542 );

        // 3秒かけて実行させます
        objectAnimator.setDuration( 300 );

        // アニメーションを開始します
        objectAnimator.start();
    }
    private void animateTranslationXLeft( View target ) {

        // translationXプロパティを0fから200fに変化させます
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat( target, "translationX", 0f, 542);

        // 3秒かけて実行させます
        objectAnimator.setDuration( 300 );

        // アニメーションを開始します
        objectAnimator.start();
    }
}