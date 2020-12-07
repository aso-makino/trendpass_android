package com.example.trendpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.trendpass.async.AsyncDispRankingActivity;
import com.example.trendpass.async.AsyncDispSpotListActivity;
import com.example.trendpass.async.AsyncMyPageReviewActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class DispSpotListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_spot_list);

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = loginData.getString("userId", "");

        try {
            String ip= getString(R.string.ip);
            new AsyncDispSpotListActivity(DispSpotListActivity.this)
                    .execute(new URL("http://"+ip+":8080/trendpass/SpotListServlet?userId=" + userId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Button rankingBtn = findViewById(R.id.dispRankingBtn);
        rankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String ip= getString(R.string.ip);
                    new AsyncDispRankingActivity(DispSpotListActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/RankingServlet"));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button dispPassBtn = findViewById(R.id.dispPassBtn);
        dispPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                //設定画面へ
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
                        .setPositiveButton("現在地からスポット投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //現在地周辺スポット一覧画面へ
//                                Intent intent = new Intent(DispSpotListActivity.this, NearSpotListActivity.class);
//                                intent.putExtra("latitude",latitude);
//                                intent.putExtra("longitude",longitude);
//                                startActivity(intent);
//                                Log.v("Alert", "スポット一覧へ");
                                ///////////////////////////
                            }
                        })



                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ////////////////////////////
                                //位置情報履歴画面へ
//                        Intent intent = new Intent(DispSpotListActivity.this,Activity.class);
//                        startActivity(intent);

                            }
                        })
                        // 口コミ投稿ボタンの処理
                        .setNegativeButton("口コミ投稿", new DialogInterface.OnClickListener() {
                            @Override
                            //口コミ投稿ボタンをタッチ
                            public void onClick(DialogInterface dialog, int which) {
                                ////////////////////////////
                                //口コミ投稿画面へ
//                                Intent intent = new Intent(DispSpotListActivity.this, Activity.class);
//                                startActivity(intent);
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
}
