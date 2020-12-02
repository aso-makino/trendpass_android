package com.example.trendpass;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.trendpass.async.AsyncGetUserInfoActivity;
import com.example.trendpass.async.AsyncMailCheckActivity;
import com.example.trendpass.async.AsyncPostBaseActivity;
import com.example.trendpass.async.AsyncSignChangeActivity;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SignChangeActivity extends AppCompatActivity {

    private InputMethodManager inputMethodManager;
    private ConstraintLayout mainLayout;
    private boolean namePermitFlg;
    private boolean mailPermitFlg;
    private boolean pass1PermitFlg;
    private boolean pass2PermitFlg;
    private boolean birthPermitFlg;

    private static final int REQUEST_GALLERY = 0;
    private Bitmap imgView;
    private String picturePath;//画像パス
    private Uri userIconUri;

    //UseIDをデバイス内から参照
    //　ユーザーIDを取得
//    SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
//    String userId = loginData.getString("userId", "");

    private String userId = "0000001";//テスト用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_change);

        //async
        String ip = getString(R.string.ip);
        String postJson = "{\"userId\":\"" + userId + "\"}";
        AsyncGetUserInfoActivity asyncGetUserInfoActivity = new AsyncGetUserInfoActivity(SignChangeActivity.this);
        asyncGetUserInfoActivity.execute("http://" + ip + ":8080/trendpass/GetUserInfo", postJson);

        //edittextの取得
        final EditText nameEtxt = findViewById(R.id.userNameET);
        final EditText mailEtxt = findViewById(R.id.mailET);
        final EditText pass1Etxt = findViewById(R.id.password1ET);
        final EditText pass2Etxt = findViewById(R.id.password2ET);
        final EditText birthEtxt = findViewById(R.id.birthET);

        final RadioGroup group = (RadioGroup)findViewById(R.id.radiogroup_sex);

        //ロゴ
        Picasso.with(this.getApplicationContext())
                .load(R.drawable.rogo)
                .resize(500, 500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.rogoImg));




        //画面全体のレイアウト
        mainLayout = findViewById(R.id.constraintLayout);
        mainLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //キーボード表示を制御するためのオブジェクト
                inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

            }
        });

        //nameの入力チェック
        nameEtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean flag) {
                if (!flag) {
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                    String name = nameEtxt.getText().toString().trim();
                    if(name.length() > 30){
                        nameEtxt.setError("文字数の上限を超えています");
                        namePermitFlg = false;
                    }else{
                        namePermitFlg = true;
                    }

                }
            }
        });

        //mailの入力チェック
        mailEtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean flag) {
                if (!flag) {
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    String pattern = "^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)+$";
                    Pattern p = Pattern.compile(pattern);
                    String mail = mailEtxt.getText().toString().trim();

                    if (!p.matcher(mail).find()) {
                        mailEtxt.setError("メールアドレスが正しく入力されていません");
                        mailPermitFlg = false;
                    }else{
                        mailPermitFlg = true;
                    }
                    if(mail.length() > 256){
                        mailEtxt.setError("文字数の上限を超えています");
                        mailPermitFlg = false;
                    }else{
                        mailPermitFlg = true;
                    }

                    String json = "{\"userId\":\"" + userId + "\",\"mail\":\"" + mail + "\"}";

                    String ip= getString(R.string.ip);
                    AsyncMailCheckActivity asyncMailCheckActivity = new AsyncMailCheckActivity(SignChangeActivity.this);
                    asyncMailCheckActivity.execute("http://"+ip+":8080/trendpass/MailCheck",json);

                }
            }
        });

        //passwordの入力チェック
        pass1Etxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean flag) {
                if (!flag) {
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    String pass1 = pass1Etxt.getText().toString().trim();
                    if(pass1.length() < 8){
                        pass1Etxt.setError("パスワードは8桁以上入力してください");
                        pass1PermitFlg = false;
                    }else{
                        pass1PermitFlg = true;
                    }
                    if(pass1.length() > 128){
                        pass1Etxt.setError("文字数の上限を超えています");
                        pass1PermitFlg = false;
                    }else{
                        pass1PermitFlg = true;
                    }
                }
            }
        });

        //password確認の入力チェック
        pass2Etxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean flag) {
                if (!flag) {
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    String pass1 = pass1Etxt.getText().toString().trim();
                    String pass2 = pass2Etxt.getText().toString().trim();
                    if(!pass1.equals(pass2)){
                        pass2Etxt.setError("パスワードが一致しません");
                        pass2PermitFlg = false;
                    }else{
                        pass2PermitFlg = true;
                    }
                }
            }
        });

        //birthの入力チェック
        birthEtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean flag) {
                if (!flag) {
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    String birth = birthEtxt.getText().toString().trim();
                    final DateFormat df = new SimpleDateFormat("yyyy");
                    final Date date = new Date(System.currentTimeMillis());
                    Date birthDate = null;
                    try {
                        birthDate = df.parse(birth);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(birth.length() > 4){
                        pass1Etxt.setError("文字数の上限を超えています");
                        birthPermitFlg = false;
                    }else if (date.before(birthDate)) {
                        birthEtxt.setError("正しい入力値を入れてください");
                        birthPermitFlg = false;
                    }else{
                        birthPermitFlg = true;
                    }
                }
            }
        });

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

        //次へボタンの押下時
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String name = nameEtxt.getText().toString().trim();
                String mail = mailEtxt.getText().toString().trim();
                String pass1 = pass1Etxt.getText().toString().trim();
                String pass2 = pass2Etxt.getText().toString().trim();
                int checkedId = group.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(checkedId);
                String sex = radioButton.getText().toString();
                String birth = birthEtxt.getText().toString().trim();

                String pattern = "^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)+$";
                Pattern p = Pattern.compile(pattern);

                final DateFormat df = new SimpleDateFormat("yyyy");
                final Date date = new Date(System.currentTimeMillis());
                Date birthDate = null;
                try {
                    birthDate = df.parse(birth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 未入力チェック
                if (name.length() == 0 || mail.length() == 0 || pass1.length() == 0 || pass2.length() == 0 || checkedId < 0 || birth.length() == 0) {

                    final View scrollView = findViewById(R.id.scrollView);
                    if (name.length() == 0) {
                        nameEtxt.setError("未入力です");
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, nameEtxt.getBottom());
                            }
                        });
                    } else if (mail.length() == 0) {
                        mailEtxt.setError("未入力です");
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, mailEtxt.getBottom());
                            }
                        });
                    } else if (pass1.length() == 0) {
                        pass1Etxt.setError("未入力です");
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, pass1Etxt.getBottom());
                            }
                        });
                    } else if (pass2.length() == 0) {
                        pass2Etxt.setError("未入力です");
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, pass2Etxt.getBottom());
                            }
                        });
                    } else if (checkedId < 0) {

                        final TextView sexErrTV = findViewById(R.id.sexErrTV);
                        sexErrTV.setText("未入力です");
                        sexErrTV.setTextColor(Color.RED);
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, sexErrTV.getBottom());
                            }
                        });

                    } else if (birth.length() == 0) {
                        birthEtxt.setError("未入力です");
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, birthEtxt.getBottom());
                            }
                        });
                    }
                } else if (name.length() <= 30 && mail.length() <= 256 && p.matcher(mail).find() && pass1.length() >= 8
                        && pass1.equals(pass2) && pass1.length() < 128 && pass1.equals(pass2) && birth.length() <= 4 && !date.before(birthDate)) {

                    // bitmap(Bitmap)に画像データが入っている前提
                    ByteBuffer byteBuffer = ByteBuffer.allocate(imgView.getByteCount());
                    imgView.copyPixelsToBuffer(byteBuffer);
                    byte[] byteImage = byteBuffer.array();

                    //async
                    String ip = getString(R.string.ip);
                    String postJson = "{\"userId\":\"" + userId + "\",\"userName\":\"" + name + "\",\"mail\":\"" + mail + "\",\"password\":\"" + pass2 + "\",\"sex\":\"" + sex + "\",\"birth\":\"" + birth + "\"}";
                    System.out.println(postJson);
                    AsyncSignChangeActivity asyncSignChangeActivity = new AsyncSignChangeActivity(SignChangeActivity.this);
                    asyncSignChangeActivity.execute("http://" + ip + ":8080/trendpass/SignChange",  postJson,picturePath);

//                    //マイページ画面へ遷移
//                    Intent intent = new Intent(SignChangeActivity.this, MypageActivity.class);

//                    RadioButton radioButton = findViewById(checkedId);
//                    String sex = radioButton.getText().toString();
//
//
//                    intent.putExtra("name", name);
//                    intent.putExtra("mail", mail);
//                    intent.putExtra("pass", pass2);
//                    intent.putExtra("sex", sex);
//                    intent.putExtra("birth", birth);
//                    intent.putExtra("userIcon", picturePath);
//                    startActivity(intent);


                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        mainLayout.requestFocus();

        return false;
    }


    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != data) {
            userIconUri = data.getData();

            try {
                ContentResolver contentResolver = getContentResolver();
                ImageView userIconV = (ImageView)findViewById(R.id.userIcon);

                // 選択した画像を表示
                InputStream in = contentResolver.openInputStream(data.getData());
                imgView = BitmapFactory.decodeStream(in);
                userIconV.setImageBitmap(imgView);
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

                Toast toast = Toast.makeText(SignChangeActivity.this,
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

}
