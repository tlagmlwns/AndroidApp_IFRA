package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
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
    String State, IN_time, OUT_time; //server data
    String F_Year, F_Month, F_Day, F_Time, F_TimeZone; //Datefilter data
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
                int cur_Year = currentCalendar.get(Calendar.YEAR);
                int cur_Month = currentCalendar.get(Calendar.MONTH);
                int cur_Day = currentCalendar.get(Calendar.DAY_OF_MONTH);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                String selectedDate = year + "년 " + (month + 1) + "월 " + day + "일";
                if (year == cur_Year && month == cur_Month && day == cur_Day) {
                    // 오늘 날짜를 클릭한 경우
                    if(selectedDate == currentDate){
                        get_log(cur_Year, cur_Month, cur_Day, currentCalendar);
                    }
                } else if (year > cur_Year || (year == cur_Year && month > cur_Month) ||
                        (year == cur_Year && month == cur_Month && day > cur_Day)) {
                    // 미래의 날짜를 클릭한 경우
                    IN_dateInfo.setText("결과 없음");
                    OUT_dateInfo.setText("");
                    IN_dateInfo.setTextColor(Color.GRAY);
                } else {
                    get_log(cur_Year, cur_Month, cur_Day, currentCalendar);
                }
            }
        });
    }
    public void get_log(final int year, final int month, final int day, final Calendar currentCalendar) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",mydata.getUser_id())
                .build();

        Request request = new Request.Builder()
                .url("http://15.164.120.162:5000/log") //<- 수정
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { e.printStackTrace();}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    try {
                        JSONObject json = new JSONObject(responseBody);

                        // 여기에서 JSON 데이터를 처리하고 원하는 정보 추출
                        String status = json.getString("status"); //요청에 대한 상태
                        String inner_time = json.getString("inner_time"); //출근
                        String outter_time = json.getString("outter_time"); //퇴근
                        String state = json.getString("state"); // 상태(입 /퇴)
                        Log.d("JSON Response: ", "status: " + status);
                        Log.d("JSON Response: ", "inner_time: " + inner_time);
                        Log.d("JSON Response: ", "outter_time: " + outter_time);
                        IN_time = inner_time;
                        OUT_time = outter_time;
                        State = state;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "없는듯 ㅋ", Toast.LENGTH_SHORT).show();
                    }
                } else { Log.e("Response Error", "Response Code: " + response.code());}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Datefilter(IN_time);
                        updateUI(year, month, day, currentCalendar);
                    }
                });
            }
        });
    }
    public void updateUI(int year, int month, int day, Calendar currentCalendar) {
        String selectedDate = year + "년 " + (month + 1) + "월 " + day + "일";
        IN_dateInfo.setText("입장" + " " + selectedDate + " " + IN_time);
        IN_dateInfo.setTextColor(Color.BLUE);
        if (OUT_time.toString() != null) {
            OUT_dateInfo.setText(State + " " + selectedDate + " " + OUT_time);
            OUT_dateInfo.setTextColor(Color.RED);
        }
    }
    public void Datefilter(String time_data){
        //String dateString = "Wed, 18 Oct 2023 09:10:00 GMT";
        String dateString = time_data;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        try {
            Date date = sdf.parse(dateString);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.ENGLISH); // 요일을 얻기 위한 포맷 지정
            SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd", Locale.ENGLISH);  // 일을 얻기 위한 포맷 지정
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH); // 달을 얻기 위한 포맷 지정
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH); // 시간을 얻기 위한 포맷 지정
            SimpleDateFormat timeZoneFormat = new SimpleDateFormat("z", Locale.ENGLISH); // 시간대 정보를 얻기 위한 포맷 지정

            // 각각의 포맷을 사용하여 요일, 일, 달, 시간, 시간대 정보를 추출합니다.
            F_Year = dayFormat.format(date);            System.out.println("년: " + F_Year);
            F_Month = monthFormat.format(date);         System.out.println("달: " + F_Month);
            F_Day = dayOfMonthFormat.format(date);      System.out.println("일: " + F_Day);
            F_Time= timeFormat.format(date);            System.out.println("시간: " + F_Time);
            F_TimeZone = timeZoneFormat.format(date);   System.out.println("시간대: " + F_TimeZone);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}