package com.maruonan.garbagecollection;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maruonan.garbagecollection.bean.UserBean;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private UserBean mUser;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Boolean isChecked;
    private String mobile;
    private String password;

    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.cb_remember) CheckBox _remberCheck;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        LitePal.getDatabase();
        mUser = DataSupport.find(UserBean.class, 1);
        if (mUser != null){
            _signupLink.setEnabled(false);
        }
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        isChecked = pref.getBoolean(CommonValues.ISREMEMBER, false);
        _remberCheck.setChecked(isChecked);
        if (isChecked){
            mobile = pref.getString(CommonValues.MOBILE, "");
            password = pref.getString(CommonValues.PASSWORD, "");
            _mobileText.setText(mobile);
            _passwordText.setText(password);
        }
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    @OnClick(R.id.cb_remember)
    public void remember() {
        isChecked = _remberCheck.isChecked();
        editor = pref.edit();
        editor.putBoolean(CommonValues.ISREMEMBER, isChecked);
        editor.apply();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        if (mUser == null){
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登录中...");
        progressDialog.show();

        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (mUser.getTelNum().equals(mobile)&&mUser.getPassword().equals(password)){
                            onLoginSuccess();
                        }else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, CommonValues.DELAYMILLIS);
    }

    @OnClick(R.id.link_signup)
    public void signup(){
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                mUser = DataSupport.find(UserBean.class, 1);
                _signupLink.setEnabled(false);
                _mobileText.setText(mUser.getTelNum());
                _passwordText.setText(mUser.getPassword());
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        if (isChecked){
            editor = pref.edit();
            editor.putString(CommonValues.MOBILE, mobile);
            editor.putString(CommonValues.PASSWORD, password);
            editor.apply();
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_SHORT).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        if (mobile.isEmpty() || mobile.length()!=11) {
            _mobileText.setError("手机号不合法");
            valid = false;
        } else {
            _mobileText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("密码长度为4~10");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


}
