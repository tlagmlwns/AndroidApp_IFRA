package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity7 extends AppCompatActivity {
    ImageButton ulc_Mypage;
    ImageButton ulc_Back;
    CalendarView calendarview;
    TextView dateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_7_logcheck);
        //-------------------------------------------------
        ulc_Back = findViewById(R.id.ibtn_ulcBack);
        //-------------------------------------------------
        ulc_Mypage = findViewById(R.id.ibtn_ulcmypage);
        //-------------------------------------------------
        calendarview = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 9, 1);
        long millis = calendar.getTimeInMillis();
        calendarview.setDate(millis);
        //-------------------------------------------------
        dateInfo = findViewById(R.id.tv_dateInfo);
        ulc_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();}
        });
        ulc_Mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ulcmypage=new Intent(getApplicationContext(), MainActivity6.class);
                startActivity(ulcmypage);
            }
        });
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) { //로그 미구현시 테스트용
                // 선택된 날짜 정보를 가지고 원하는 동작 수행
                Calendar currentCalendar = Calendar.getInstance();
                Random rand = new Random();
                int cur_Year = currentCalendar.get(Calendar.YEAR);
                int cur_Month = currentCalendar.get(Calendar.MONTH);
                int cur_Day = currentCalendar.get(Calendar.DAY_OF_MONTH);
                String selectedDate = year + "년 " + (month + 1) + "월 " + day + "일";
                if (year == cur_Year && month == cur_Month && day == cur_Day) {
                    // 오늘 날짜를 클릭한 경우
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDate = sdf.format(new Date());
                    int test = rand.nextInt(3); //0,1,2
                    if (test == 0) {
                        dateInfo.setText("- 선택된 날짜: " + currentDate + " 출석 "); //
                        dateInfo.setTextColor(Color.GREEN);
                    }
                    else if (test == 1) {
                        dateInfo.setText("- 선택된 날짜: " + currentDate + " 지각 "); //
                        dateInfo.setTextColor(Color.YELLOW);
                    }
                    else if (test == 2) {
                        dateInfo.setText("- 선택된 날짜: " + currentDate + " 결석 "); //
                        dateInfo.setTextColor(Color.RED);
                    }
                } else if (year > cur_Year || (year == cur_Year && month > cur_Month) ||
                        (year == cur_Year && month == cur_Month && day > cur_Day)) {
                    // 미래의 날짜를 클릭한 경우
                    dateInfo.setText("결과 없음");
                    dateInfo.setTextColor(Color.GRAY);
                } else {
                    int test = rand.nextInt(3); //0,1,2
                    if (test == 0) {
                        dateInfo.setText("- 선택된 날짜: " + selectedDate + " 출석 "); //
                        dateInfo.setTextColor(Color.GREEN);
                    }
                    else if (test == 1) {
                        dateInfo.setText("- 선택된 날짜: " + selectedDate + " 지각 "); //
                        dateInfo.setTextColor(Color.YELLOW);
                    }
                    else if (test == 2) {
                        dateInfo.setText("- 선택된 날짜: " + selectedDate + " 결석 "); //
                        dateInfo.setTextColor(Color.RED);
                    }
                }
                // 여기에 선택된 날짜에 따른 추가 동작을 수행할 수 있습니다.
                // 예를 들어 선택된 날짜에 해당하는 데이터를 불러오거나 처리할 수 있습니다.
            }
        });
    }
}