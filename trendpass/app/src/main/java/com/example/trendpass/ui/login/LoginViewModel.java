package com.example.trendpass.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.util.Patterns;
import android.widget.TextView;

import com.example.trendpass.async.AsyncLoginActivity;
import com.example.trendpass.data.AsyncTaskCallbackListener;
import com.example.trendpass.data.LoginRepository;
import com.example.trendpass.data.Result;
import com.example.trendpass.data.model.LoggedInUser;
import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginViewModel extends ViewModel implements AsyncTaskCallbackListener {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, Activity activity) {
        // can be launched in a separate asynchronous job
        try {
            String ip= "";
            new AsyncLoginActivity(activity, LoginViewModel.this)
                    .execute(new URL("http://" + ip + ":8080/trendpass/AuthServlet?email=" + username + "&password=" + password));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void onPostExecute(JSONObject resJson) {

        Activity activity = null;
        LoggedInUser user = null;
        String userId = "";
        String userName = "";
        String userIcon = "";
        Result<LoggedInUser> result;

        try {
            try {
                if (!resJson.isNull("loginInfo")) {
                    //　取得できていたらクラス変数に格納
                    userId = resJson.getJSONObject("loginInfo").getString("userId");
                    userName = resJson.getJSONObject("loginInfo").getString("userName");
                    userIcon = resJson.getJSONObject("loginInfo").getString("userIcon");

                } else {
                    // 取得に失敗していた場合、エラーメッセージを表示
                    TextView tv = activity.findViewById(R.id.LoginResultTxtv);
                    tv.setText("メールアドレス又は、パスワードが間違っています。");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            user = new LoggedInUser(userId, userName, userIcon);

            result = new Result.Success<>(user);
            //return result;

        } catch (Exception e) {
            result = new Result.Error(new IOException("Error logging in", e));
        }

        if (result instanceof Result.Success) {
            loginRepository.setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUserId(), data.getUserName(), data.getUserIcon())));

        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }
}
