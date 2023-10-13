package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity4 extends AppCompatActivity {
    ImageButton um_Mypage; //um_Mypage : User Main -> Mypage
    Button um_Logcheck; //um_Logcheck : User Main -> Logcheck
    Button Inquiry;
    CountDownTimer countDownTimer;
    TextView timeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_4_umain);

        um_Mypage = findViewById(R.id.ibtn_mypage);
        um_Logcheck = findViewById(R.id.btn_lcuser);
        Inquiry = findViewById(R.id.btn_Inquiry);
        timeTextView = findViewById(R.id.tv_Time);
        um_Mypage.setOnClickListener(new View.OnClickListener() { //마이페이지 바로가기
            @Override
            public void onClick(View view) {
                Intent ummypage=new Intent(getApplicationContext(), MainActivity6.class);
                startActivity(ummypage);
            }
        });
        um_Logcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logcheck=new Intent(getApplicationContext(), MainActivity7.class);
                startActivity(logcheck);
            }
        });
        Inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity4.this);
                builder.setTitle("안내")
                        .setIcon(R.drawable.dap)
                        .setMessage("버그신고 및 문의는 관리자에게 문의하거나 이메일로 보내주세요.\n-문의 이메일 : simh4jun@gmail.com\n-관리자 번호 : 010-1234-5678")
                        .setPositiveButton("확인", null);
                AlertDialog dialog = builder.create();
                dialog.show();
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