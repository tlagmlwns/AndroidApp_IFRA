package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity4 extends AppCompatActivity {
    private TextView timeTextView;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        ImageButton ibtn_mypage = findViewById(R.id.ibtn_mypage);
        timeTextView = findViewById(R.id.tv_Time);
        ibtn_mypage.setOnClickListener(new View.OnClickListener() { //마이페이지 바로가기
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity6.class);
                startActivity(intent);
            }
        });
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) { //메인페이지 현재날짜띄우기
            public void onTick(long millisUntilFinished) {
                updateTime();
            }
            public void onFinish() {
                // 작업을 종료할 때의 동작
            }
        }.start();
    }
    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        String cur_Date = dateFormat.format(new Date());
        timeTextView.setText(cur_Date);
    }
}