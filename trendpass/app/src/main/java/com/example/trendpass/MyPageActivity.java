package com.example.trendpass;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncMyPageReviewActivity;
import com.example.trendpass.async.AsyncMyPageSpotActivity;
import com.google.android.gms.internal.zzcfy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.view.View.OnClickListener;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MyPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        //タブの下線を左にずらす
        View tabBar = findViewById(R.id.tabBar);
        animateTranslationStart(tabBar);

        //設定ボタン表示
        Picasso.with(this)
                .load(R.drawable.settings)
                .fit()
                .centerInside()
                .into((ImageView) findViewById(R.id.settings_btn));

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = loginData.getString("userId", "");

        try {
            String ip= getString(R.string.ip);
            new AsyncMyPageReviewActivity(MyPageActivity.this)
                    .execute(new URL("http://"+ip+":8080/trendpass/MyPageServlet?userId=" + userId));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //画像タップ時に拡大
        final ImageView usericon = (ImageView)findViewById(R.id.user_icon);
        usericon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(MyPageActivity.this);
                Bitmap bitmap = ((BitmapDrawable)usericon.getDrawable()).getBitmap();
                imageView.setImageBitmap(bitmap);
                // ディスプレイの幅を取得する（API 13以上）
                Display display =  getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                float factor =  width / bitmap.getWidth();
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                // ダイアログを作成する
                Dialog dialog = new Dialog(MyPageActivity.this);
                // タイトルを非表示にする
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(imageView);
                dialog.getWindow().setLayout((int)(bitmap.getWidth()*factor), (int)(bitmap.getHeight()*factor));
                // ダイアログを表示する
                dialog.show();
            }
        });

    }

    protected void onResume() {
        super.onResume();

        final Button dispReviewBtn = findViewById(R.id.dispRankingBtn);
        final Button dispSpotBtn = findViewById(R.id.dispPassBtn);

        //最初は口コミが表示されているので、口コミボタンを押せないようにする
        dispReviewBtn.setEnabled(false);

        dispReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //タブの下線切り替え
                    View tabBar = findViewById(R.id.tabBar);
                    animateTranslationXRight(tabBar);

                    //口コミボタンを押せなくし、色を青に
                    dispReviewBtn.setEnabled(false);
                    dispReviewBtn.setTextColor(Color.parseColor("#2196F3"));

                    //スポットボタンを押せるようにし、色を灰色に
                    dispSpotBtn.setEnabled(true);
                    dispSpotBtn.setTextColor(Color.parseColor("#5E5E5E"));

                    //　ユーザーIDを取得
                    SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
                    String userId = loginData.getString("userId", "");

                    String ip= getString(R.string.ip);
                    new AsyncMyPageReviewActivity(MyPageActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/MyPageServlet?userId=" + userId));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        dispSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //タブの下線切り替え
                    View tabBar = findViewById(R.id.tabBar);
                    animateTranslationXLeft(tabBar);

                    //スポットボタンを押せなくし、色を青に
                    dispSpotBtn.setEnabled(false);
                    dispSpotBtn.setTextColor(Color.parseColor("#2196F3"));

                    //口コミボタンを押せるようにし、色を灰色に
                    dispReviewBtn.setEnabled(true);
                    dispReviewBtn.setTextColor(Color.parseColor("#5E5E5E"));

                    //　ユーザーIDを取得
                    SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
                    String userId = loginData.getString("userId", "");

                    String ip= getString(R.string.ip);
                    new AsyncMyPageSpotActivity(MyPageActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/MyPageServlet?userId=" + userId));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton settingsBtn = findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                Intent intent = new Intent(MyPageActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(MyPageActivity.this)
                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //位置情報履歴画面へ
                                Intent intent = new Intent(MyPageActivity.this,NearBySpotsListActivity.class);
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
                                Intent intent = new Intent(MyPageActivity.this, NearSpotListActivity.class);
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
                    Intent intent = new Intent(MyPageActivity.this, DispSpotListActivity.class);
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
                Intent intent = new Intent(MyPageActivity.this, DispMapActivity.class);
                startActivity(intent);
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

