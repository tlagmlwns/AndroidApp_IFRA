package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity8 extends AppCompatActivity {
    ImageButton mpBack;
    ImageButton mpMypage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_8_);

        mpBack = findViewById(R.id.ibtn_mpBack);
        mpMypage = findViewById(R.id.ibtn_mpmypage);

        mpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();}
        });
        mpMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mpmypage=new Intent(getApplicationContext(), MainActivity6.class);
                startActivity(mpmypage);
            }
        });
    }
}