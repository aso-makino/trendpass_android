package com.example.trendpass;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.trendpass.async.AsyncSpotDeleteActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

public class ReviewDetailActivity extends AppCompatActivity {


    private String spotId;
    private String reviewNumber;
    private String reviewUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = loginData.getString("userId", "");

        Intent intent = getIntent();
        // intentで受け取ったものを取り出す
        spotId = intent.getStringExtra("spotId");
        reviewNumber = intent.getStringExtra("reviewNumber");
        String reviewRatingStr = intent.getStringExtra("rating");
        int reviewRating = Integer.parseInt(reviewRatingStr);
        String reviewContent = intent.getStringExtra("reviewContent");
        String reviewImage = intent.getStringExtra("reviewImage");
        String spotName = intent.getStringExtra("spotName");
        reviewUserId = intent.getStringExtra("reviewUserId");

        Button deleteBtn = (Button) findViewById(R.id.delete_button);
        if(userId.equals(reviewUserId)) {
            deleteBtn.setVisibility(View.VISIBLE);
        }else{
            deleteBtn.setVisibility(View.INVISIBLE);

        }


        //スポット名
        TextView spotNametv = this.findViewById(R.id.spotName);
        spotNametv.setText(spotName);
        spotNametv.setTextSize(22.0f);

        //口コミ画像
        String ip= getString(R.string.ip);
        Picasso.with(this.getApplicationContext())
                .load("http://"+ip+":8080/trendpass/DisplayImage?name=" +reviewImage)
                .resize(500,500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.spotImg));

        //評価
        RatingBar ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                Drawable progressDrawable = ratingBar.getProgressDrawable();
                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable, ContextCompat.getColor(ratingBar.getContext(), R.color.ratingbar));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ratingBar.setRating(reviewRating);


        //口コミ内容
        TextView textView = findViewById(R.id.reviewContent);
        textView.setText(reviewContent);

        //画像タップ時に拡大
        final ImageView reviewImageV = (ImageView)findViewById(R.id.spotImg);
        reviewImageV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(ReviewDetailActivity.this);
                Bitmap bitmap = ((BitmapDrawable)reviewImageV.getDrawable()).getBitmap();
                imageView.setImageBitmap(bitmap);
                // ディスプレイの幅を取得する（API 13以上）
                Display display =  getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                float factor =  width / bitmap.getWidth();
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                // ダイアログを作成する
                Dialog dialog = new Dialog(ReviewDetailActivity.this);
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

        Button deleteBtn = findViewById(R.id.delete_button);
        //削除ボタンをタッチした時の処理
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチしたら確認ダイアログ
            public void onClick(View view) {
                new AlertDialog.Builder(ReviewDetailActivity.this)
                        .setTitle( "レビュー削除確認" )
                        .setMessage( "レビューを削除します。\nよろしいですか" )
                        .setIcon( R.drawable.rogo )
                        .setPositiveButton( "削除", new  DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // クリックしたときの処理
                                AsyncSpotDeleteActivity asyncSpotDeleteActivity = new AsyncSpotDeleteActivity(ReviewDetailActivity.this);
                                try {
                                    String ip= getString(R.string.ip);
                                    asyncSpotDeleteActivity.execute(new URL("http://"+ip+":8080/trendpass/DeleteReviewServlet?spotId=" +spotId+"&reviewNumber="+reviewNumber+"&userId="+reviewUserId));
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("キャンセル", new  DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // クリックしたときの処理
                                dialog.dismiss();
                            }
                        })
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
                Intent intent = new Intent(ReviewDetailActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(ReviewDetailActivity.this)
                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //位置情報履歴画面へ
                                Intent intent = new Intent(ReviewDetailActivity.this,NearBySpotsListActivity.class);
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
                                Intent intent = new Intent(ReviewDetailActivity.this, NearSpotListActivity.class);
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
                Intent intent = new Intent(ReviewDetailActivity.this, DispSpotListActivity.class);
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
                Intent intent = new Intent(ReviewDetailActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });


    }
}
