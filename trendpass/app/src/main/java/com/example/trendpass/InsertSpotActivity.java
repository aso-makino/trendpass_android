package com.example.trendpass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.trendpass.async.AsyncInsert;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.InputStream;

public class InsertSpotActivity extends AppCompatActivity {

    // ジャンル選択肢
    private String[] spinnerItems = {"駅チカ", "レジャー施設", "野外施設", "グルメ"};
    private int genreNumber = 0;//int型でサーバーに送信する

    //現在地取得のための変数宣言
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;


    //保存済み位置情報をデバイス内から参照
    //SharedPreferences  restore_spot = PreferenceManager.getDefaultSharedPreferences( this );
    //Map<String,?> map = restore_spot.getAll();
    //String key;
    //Object value;
    //ArrayList<String> geolocationList = new ArrayList<>();
    //for( Entry<String, ?> entry : map.entrySet() )
    //{
    // key = entry.getKey();
    // value = entry.getValue();
    // geoLocationList.add(value);
    //}

    //保存済み位置情報メモをデバイス内から参照
    //SharedPreferences  restore_spotmemo = PreferenceManager.getDefaultSharedPreferences( this );
    //Map<String,?> map = restore_spotmemo.getAll();
    //String key;
    //Object value;
    //ArrayList<String> memoList = new ArrayList<>();
    //for( Entry<String, ?> entry : map.entrySet() )
    //{
    // key = entry.getKey();
    // value = entry.getValue();
    // memoList.add(value);
    //}

    //変数
    private TextView textView;
    private String genre;
    private int ratingNumber;//RatingBarもといratingBarは予約語のため別変数用意
    private String userId;


    // カメラ & 画像選択ダイアログ用
    private static final int REQUEST_GALLERY = 0;
    private Bitmap imgView;
    private String picturePath;//画像パス
    private Uri userIconUri;


////////////////////////////////////////////////////////////////////////////////////////////////////
// 汎用メソッド
////////////////////////////////////////////////////////////////////////////////////////////////////

    // パーミッションダイアログ
    public boolean CheckPermission(Activity actibity, String permission, int requestCode) {
        // 権限の確認
        if (ActivityCompat.checkSelfPermission(actibity, permission) !=
                PackageManager.PERMISSION_GRANTED) {

            // 権限の許可を求めるダイアログを表示する
            ActivityCompat.requestPermissions(actibity, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_spot_activity);

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        userId = loginData.getString("userId", "");

        //現在地取得の設定
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        @SuppressLint("RestrictedApi")
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(
// どれにするかはお好みで、ただしできない状況ではできないので
//                LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//                LocationRequest.PRIORITY_LOW_POWER);
//                LocationRequest.PRIORITY_NO_POWER);

        //現在地の取得
        getLastLocation();

        Spinner spinner = findViewById(R.id.spinner);

        // ArrayAdapter
        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        spinner.setAdapter(adapter);

        // リスナーを登録
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                genre = (String) spinner.getSelectedItem();

                /*選択されたジャンルからジャンルナンバーを取得*/
                if (genre.equals("駅チカ")) {
                    genreNumber = 1;
                } else if (genre.equals("レジャー施設")) {
                    genreNumber = 2;
                } else if (genre.equals("野外施設")) {
                    genreNumber = 3;
                } else if (genre.equals("グルメ")) {
                    genreNumber = 4;
                }
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        /*レーティングバー周りの処理*/

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar_spot);
        // ☆の最大数の設定及び取得
        ratingBar.setNumStars(5);
        ratingNumber = ratingBar.getNumStars();
        // 現在のレイティングの設定及び取得
        ratingBar.setRating(0);
        float rating = ratingBar.getRating();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*画像アップロード*/

        //画像選択ボタン押下時
        Button getBtn = (Button) findViewById(R.id.Image_button);
        getBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // ギャラリー呼び出し
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        /*EC2サーバーに情報を送信する*/
        //投稿ボタン
        Button sendBtn = (Button) findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String ip = getString(R.string.ip);;
                //String ip = "";//AWSのEC2のパブリックエンドポイントを設定
                //入力されたスポット名を取得
                EditText eText = (EditText) findViewById(R.id.spotname);
                String spotName = eText.getText().toString();
                //入力されたスポットレビューを取得
                EditText eText2 = (EditText) findViewById(R.id.review);
                String spotReview = eText2.getText().toString();

                //GET通信-URL の後に ? に続いて パラメータ名=値&パラメータ名=値 の形式で付加します。
                    /*
                    * @param spotName スポット名
                    * @param spotReview スポットのレビュー
                    * @param ratingNumber スポット評価☆
                    * @param userId　ユーザーID
                    * @param genreNumber　ジャンル番号
                    * @param latitude　緯度
                    * @param longitude　経度
                    * @param picturePath　画像パス
                    */
                String postJson = "{\"spotName\":\"" + spotName + "\",\"genreId\":\"" + genreNumber + "\",\"rating\":\"" + ratingNumber + "\",\"spotReview\":\"" + spotReview + "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\",\"userId\":\"" + userId + "\"}";
                System.out.println(postJson);
                //Asyncクラスに接続
                    new AsyncInsert(InsertSpotActivity.this)
                            .execute("http://" + ip + ":8080/trendpass/InsertSpot",postJson,picturePath);

            }
        });
    }

    /*
    画像処理メソッド
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != data) {
            userIconUri = data.getData();

            try {
                ContentResolver contentResolver = getContentResolver();
                ImageView spotImgV = (ImageView)findViewById(R.id.spotImg);

                // 選択した画像を表示
                InputStream in = contentResolver.openInputStream(data.getData());
                imgView = BitmapFactory.decodeStream(in);
                spotImgV.setImageBitmap(imgView);
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
                        null,null,null,null);

                if (cursor != null && cursor.moveToFirst()) {
                    String str =  String.format(
                            "MediaStore.Images = %s\n\n", cursor.getCount() );

                    picturePath = cursor.getString(cursor.getColumnIndex(
                            MediaStore.Images.Media.DATA));
                    cursor.close();


                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(InsertSpotActivity.this,
                        "例外が発生、Permissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
                finish();
            } finally{
                if(cursor != null){
                    cursor.close();
                }
            }
        }
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
                                } else {
                                    Log.d("debug","計測不能");
                                    System.out.println("計測不能");
                                }
                            }
                        });
    }
}

