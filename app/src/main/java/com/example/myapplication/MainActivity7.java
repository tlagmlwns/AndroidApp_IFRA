package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity7 extends AppCompatActivity {
    Single_ton_data mydata = Single_ton_data.getInstance();
    ImageButton ulc_Mypage;
    ImageButton ulc_Back;
    CalendarView calendarview;
    TextView IN_dateInfo, OUT_dateInfo;
    String date_result;

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
        IN_dateInfo = findViewById(R.id.tv_dateInfo1);
        OUT_dateInfo = findViewById(R.id.tv_dateInfo2);
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
                        IN_dateInfo.setText("- 선택된 날짜: " + currentDate + " 출석 "); //
                        IN_dateInfo.setTextColor(Color.GREEN);
                    }
                    else if (test == 1) {
                        IN_dateInfo.setText("- 선택된 날짜: " + currentDate + " 지각 "); //
                        IN_dateInfo.setTextColor(Color.YELLOW);
                    }
                    else if (test == 2) {
                        IN_dateInfo.setText("- 선택된 날짜: " + currentDate + " 결석 "); //
                        IN_dateInfo.setTextColor(Color.RED);
                    }
                } else if (year > cur_Year || (year == cur_Year && month > cur_Month) ||
                        (year == cur_Year && month == cur_Month && day > cur_Day)) {
                    // 미래의 날짜를 클릭한 경우
                    IN_dateInfo.setText("결과 없음");
                    IN_dateInfo.setTextColor(Color.GRAY);
                } else {
                    int test = rand.nextInt(3); //0,1,2
                    if (test == 0) {
                        IN_dateInfo.setText("- 선택된 날짜: " + selectedDate + " 출석 "); //
                        IN_dateInfo.setTextColor(Color.GREEN);
                    }
                    else if (test == 1) {
                        IN_dateInfo.setText("- 선택된 날짜: " + selectedDate + " 지각 "); //
                        IN_dateInfo.setTextColor(Color.YELLOW);
                    }
                    else if (test == 2) {
                        IN_dateInfo.setText("- 선택된 날짜: " + selectedDate + " 결석 "); //
                        IN_dateInfo.setTextColor(Color.RED);
                    }
                }
                // 여기에 선택된 날짜에 따른 추가 동작을 수행할 수 있습니다.
                // 예를 들어 선택된 날짜에 해당하는 데이터를 불러오거나 처리할 수 있습니다.
            }
        });
    }
    public void getlog() { // 서버로 보내기 수정중
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", mydata.getUser_id())
                .build();

        Request request = new Request.Builder()
                .url("http://15.164.120.162:5000/????") //<- 수정
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    try {
                        JSONObject json = new JSONObject(responseBody);

                        // 여기에서 JSON 데이터를 처리하고 원하는 정보 추출
                        String status = json.getString("status");
                        String result = json.getString("result");

                        Log.d("JSON Response: ", "status: " + status);
                        Log.d("JSON Response: ", "result: " + result);
                        date_result = result;
                        OUT_dateInfo.setText(date_result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "없는듯 ㅋ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 응답이 성공적이지 않은 경우에 대한 처리
                    Log.e("Response Error", "Response Code: " + response.code());
                }
            }
        });
    }
}