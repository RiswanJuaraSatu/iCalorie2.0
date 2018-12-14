package com.apps.riswanda.icalorie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GetStarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
    }


    public void BtnSignUp(View view) {
        Intent intent = new Intent(GetStarted.this,SignUp.class);
        startActivity(intent);
    }

    public void btnLogin(View view) {
        Intent intent = new Intent(GetStarted.this,LoginActivity.class);
        startActivity(intent);
    }
}
