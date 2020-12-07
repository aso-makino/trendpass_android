package com.example.trendpass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncSpotDetailActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class DispSpotDetailActivity extends AppCompatActivity{

    AsyncSpotDetailActivity asyncSpotDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_spot_detail);

        Intent intent = getIntent();
        String spotId = intent.getStringExtra("spotId");

        try {
            String ip= getString(R.string.ip);
            asyncSpotDetailActivity = new AsyncSpotDetailActivity(DispSpotDetailActivity.this);
            asyncSpotDetailActivity.execute(new URL("http://"+ip+":8080/trendpass/DispSpotDetail?spotId=" + spotId));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    protected void onResume() {
        super.onResume();

        ListView listView = (ListView)findViewById(R.id.reviewList);

        //リスト項目をクリック時に呼び出されるコールバックを登録
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //リスト項目クリック時の処理
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent intent = new Intent(DispSpotDetailActivity.this, ReviewDetailActivity.class);
                // clickされたpositionのtextとphotoのID
                String reviewImage = asyncSpotDetailActivity.getReviewImage(position);
                int reviewRating = asyncSpotDetailActivity.getReviewRating(position);
                String reviewContent = asyncSpotDetailActivity.getReviewContent(position);
                String spotName = asyncSpotDetailActivity.getSpotName();

                // インテントにセット
                intent.putExtra("reviewImage",reviewImage);
                intent.putExtra("reviewRating", reviewRating);
                intent.putExtra("reviewContent", reviewContent);
                intent.putExtra("spotName",spotName);
                // Activity をスイッチする
                startActivity(intent);

                //今回は、トースト表示
                //ListView listView =(ListView)parent;
                //String item=(String)listView.getItemAtPosition(position);
                //Toast.makeText(DispSpotDetailActivity.this, "Click: "+item, Toast.LENGTH_SHORT).show();
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
                        .setPositiveButton("現在地からスポット投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //現在地周辺スポット一覧画面へ
//                                Intent intent = new Intent(DispSpotDetailActivity.this, NearSpotListActivity.class);
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
//                        Intent intent = new Intent(DispSpotDetailActivity.this,Activity.class);
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
//                                Intent intent = new Intent(DispSpotDetailActivity.this, Activity.class);
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

