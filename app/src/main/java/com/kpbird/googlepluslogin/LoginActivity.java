package com.kpbird.googlepluslogin;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class LoginActivity extends ActionBarActivity implements GooglePlusLoginUtils.GPlusLoginStatus {

    private String TAG = "LoginActivity";
    private GooglePlusLoginUtils gLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gLogin = new GooglePlusLoginUtils(this, R.id.activity_login_gplus);
        gLogin.setLoginStatus(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        gLogin.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        gLogin.disconnect();
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        gLogin.onActivityResult(requestCode, responseCode, intent);

    }

    @Override
    public void OnSuccessGPlusLogin(Bundle profile) {
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.NAME));
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.EMAIL));
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.PHOTO));
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.PROFILE));
    }
}
