package com.example.trendpass;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.trendpass.async.AsyncInsert;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class InsertSpotActivity extends AppCompatActivity {

    // 選択肢
    private String[] spinnerItems = {"駅チカ", "レジャー施設", "野外施設", "グルメ"};
    //変数
    private TextView textView;
    private int userId = 0000000;
    private String item = "";
    private int num;
    // カメラ & 画像選択ダイアログ用
    private static final int REQUEST_GALLERY = 0;
    private Bitmap imgView;
////////////////////////////////////////////////////////////////////////////////////////////////////
// 汎用メソッド
////////////////////////////////////////////////////////////////////////////////////////////////////

    // パーミッションダイアログ
    public boolean CheckPermission(Activity actibity, String permission, int requestCode){
        // 権限の確認
        if (ActivityCompat.checkSelfPermission(actibity, permission) !=
                PackageManager.PERMISSION_GRANTED) {

            // 権限の許可を求めるダイアログを表示する
            ActivityCompat.requestPermissions(actibity, new String[]{permission},requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_spot_activity);

        /*ジャンル番号をリストダウンで表示したい*/

        textView = findViewById(R.id.text_view);

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
                item = (String) spinner.getSelectedItem();
                textView.setText(item);
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
        int num = ratingBar.getNumStars();
        // 現在のレイティングの設定及び取得
        ratingBar.setRating(2.5f);
        float rating = ratingBar.getRating();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*画像アップロード*/

        Button getBtn = (Button) findViewById(R.id.btn_gallery);
        getBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String ip = getString(R.string.ip);
                //imgView = (ImageView)findViewById(R.id.imgview_id);
                // ギャラリー呼び出し
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        Button sendBtn = (Button) findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String ip = getString(R.string.ip);
                EditText eText = (EditText) findViewById(R.id.spotname);
                String name = eText.getText().toString();
                EditText eText2 = (EditText) findViewById(R.id.review);
                String review = eText2.getText().toString();

                try {
                    new AsyncInsert(InsertSpotActivity.this)
                            .execute(new URL("http://" + ip + ":8080/trendpass/SampleServlet?spotName="
                                    + name + "genreId=" + item + "rating=" + num + "review=" + review + "image=" +imgView));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                imgView = BitmapFactory.decodeStream(in);
                in.close();
                // 選択した画像を表示
                //imgView.setImageBitmap(img);
            } catch (Exception e) {
            }
        }
    }
}

