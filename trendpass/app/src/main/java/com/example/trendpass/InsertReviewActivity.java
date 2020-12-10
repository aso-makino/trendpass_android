package com.example.trendpass;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncInsert;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class InsertReviewActivity extends AppCompatActivity {

    //変数宣言
    private String userId;
    private String spotId;
    private String spotName;
    private String spotImage;
    private int num;
    private float rating;

    //画像系の変数宣言
    private static final int REQUEST_GALLERY = 0;
    private Bitmap imgView;
    private String picturePath;//画像パス
    private Uri userIconUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        userId = loginData.getString("userId", "");

        Intent intent = getIntent();
        spotId = intent.getStringExtra("spotId");
        spotName = intent.getStringExtra("spotName");
        spotImage = intent.getStringExtra("spotImage");

        /*レーティングバー周りの処理*/
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        // ☆の最大数の設定及び取得
        ratingBar.setNumStars(5);
        num = ratingBar.getNumStars();
        // 現在のレイティングの設定及び取得
        ratingBar.setRating(0);
        rating = ratingBar.getRating();

        TextView spotNametxtv = findViewById(R.id.spotName);
        spotNametxtv.setText(spotName);

        String ip = getString(R.string.ip);
        Picasso.with(InsertReviewActivity.this)
                .load("http://" + ip + ":8080/trendpass/DisplayImage?name=" + spotImage)
                .resize(500, 500)
                .placeholder(R.drawable.noimage)
                .centerCrop()
                .into((ImageView) findViewById(R.id.spotImg));
    }

    @Override
    protected void onResume() {
        super.onResume();

        //画像設定ボタンの押下時

        findViewById(R.id.Image_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // ギャラリー呼び出し
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        //口コミ投稿ボタンの押下時
        Button sendBtn = (Button) findViewById(R.id.review_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String ip = getString(R.string.ip);
                EditText eText2 = (EditText) findViewById(R.id.review2);
                String review2 = eText2.getText().toString();
                System.out.println(userId);
                System.out.println(spotId);
                System.out.println(num);
                System.out.println(review2);

                String postJson = "{\"userId\":\"" + userId + "\",\"spotId\":\"" + spotId + "\",\"rating\":\"" + num + "\",\"reviewContent\":\"" + review2 + "\"}";
                System.out.println(postJson);
                AsyncInsert asyncInsert = new AsyncInsert(InsertReviewActivity.this);
                asyncInsert.execute("http://" + ip + ":8080/trendpass/InsertReviewServlet",postJson,picturePath);

            }
        });
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != data) {
            userIconUri = data.getData();

            try {
                ContentResolver contentResolver = getContentResolver();
                ImageView reviewImgV = (ImageView) findViewById(R.id.reviewImg);

                // 選択した画像を表示
                InputStream in = contentResolver.openInputStream(data.getData());
                imgView = BitmapFactory.decodeStream(in);
                reviewImgV.setImageBitmap(imgView);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = null;

            // 例外を受け取る
            try {

                cursor = contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    String str = String.format(
                            "MediaStore.Images = %s\n\n", cursor.getCount());

                    picturePath = cursor.getString(cursor.getColumnIndex(
                            MediaStore.Images.Media.DATA));
                    cursor.close();


                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(InsertReviewActivity.this,
                        "例外が発生、Permissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
                finish();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }

    }
}
