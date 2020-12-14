package com.example.trendpass;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncSpotDeleteActivity;
import com.example.trendpass.async.AsyncSpotDetailActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class DispSpotDetailActivity extends AppCompatActivity{

    AsyncSpotDetailActivity asyncSpotDetailActivity;
    AsyncSpotDeleteActivity asyncSpotDeleteActivity;

    private String spotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_spot_detail);


        Intent intent = getIntent();
        spotId = intent.getStringExtra("spotId");

        try {
            String ip= getString(R.string.ip);
            asyncSpotDetailActivity = new AsyncSpotDetailActivity(DispSpotDetailActivity.this);
            asyncSpotDetailActivity.execute(new URL("http://"+ip+":8080/trendpass/DispSpotDetail?spotId=" + spotId));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //画像タップ時に拡大
        final ImageView spotImageV = (ImageView)findViewById(R.id.spotImg);
        spotImageV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(DispSpotDetailActivity.this);
                Bitmap bitmap = ((BitmapDrawable)spotImageV.getDrawable()).getBitmap();
                imageView.setImageBitmap(bitmap);
                // ディスプレイの幅を取得する（API 13以上）
                Display display =  getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                float factor =  width / bitmap.getWidth();
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                // ダイアログを作成する
                Dialog dialog = new Dialog(DispSpotDetailActivity.this);
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
                Intent intent = new Intent(DispSpotDetailActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(DispSpotDetailActivity.this)
                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //位置情報履歴画面へ
                                Intent intent = new Intent(DispSpotDetailActivity.this,NearBySpotsListActivity.class);
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
                                Intent intent = new Intent(DispSpotDetailActivity.this, NearSpotListActivity.class);
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
                Intent intent = new Intent(DispSpotDetailActivity.this, DispSpotListActivity.class);
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
                Intent intent = new Intent(DispSpotDetailActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });


    }

}

