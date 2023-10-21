package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity7 extends AppCompatActivity {
    Single_ton_data mydata = Single_ton_data.getInstance();
    ImageButton ulc_Mypage, ulc_Back;
    CalendarView calendarview;
    TextView IN_stats, IN_lr, OUT_stats, OUT_lr, S_Date1, S_Date2;
    String State, IN_time, OUT_time; //server data
    String selectedDate, logDate;
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
        IN_stats = findViewById(R.id.tv_Lstats_IN_result);
        IN_lr = findViewById(R.id.tv_LR_INV);
        OUT_stats = findViewById(R.id.tv_Lstats_OUT_result);
        OUT_lr = findViewById(R.id.tv_LR_OUTV);
        S_Date1 = findViewById(R.id.tv_LselectDate_result);
        S_Date2 = findViewById(R.id.tv_LselectDate_result2);
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
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // 선택된 날짜 정보를 가지고 원하는 동작 수행
                String senddate = Integer.toString(year) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(dayOfMonth) ;
                selectedDate = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 ";
                S_Date1.setText(selectedDate);
                get_log(senddate);
            }
        });
    }
    public void get_log(String sedate) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",mydata.getUser_id())
                .addFormDataPart("date",sedate)
                .build();

        Request request = new Request.Builder()
                .url("http://15.164.120.162:5000/log")
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
                    }
                } else { Log.e("Response Error", "Response Code: " + response.code());}

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Datefilter(IN_time);

                        if(selectedDate.equals(logDate)){
                            if(State.equals("퇴장")){ //최종 State가 퇴장일때
                                IN_stats.setText("입장"); IN_stats.setTextColor(Color.BLUE); //입장시 로그
                                IN_lr.setText(IN_time); IN_lr.setTextColor(Color.BLACK);

                                S_Date2.setText(selectedDate);
                                OUT_stats.setText(State); OUT_stats.setTextColor(Color.RED); //퇴장시 로그
                                OUT_lr.setText(OUT_time); OUT_lr.setTextColor(Color.BLACK);
                            }
                            else{ //최종 State가 입장일때
                                IN_stats.setText(State); IN_stats.setTextColor(Color.BLUE); //입장시 로그
                                IN_lr.setText(IN_time); IN_lr.setTextColor(Color.BLACK);

                                S_Date2.setText("");
                                OUT_stats.setText(""); OUT_stats.setTextColor(Color.RED); //퇴장시 로그
                                OUT_lr.setText(""); OUT_lr.setTextColor(Color.GRAY);
                            }
                        }
                        else{ //아예 Nodata일때
                            IN_stats.setText(""); IN_stats.setTextColor(Color.GRAY);
                            IN_lr.setText("결과없음"); IN_lr.setTextColor(Color.GRAY);

                            S_Date2.setText("");
                            OUT_stats.setText(""); OUT_lr.setTextColor(Color.GRAY);
                            OUT_lr.setText(""); OUT_lr.setTextColor(Color.GRAY);

                        }
                    }
                });
            }
        });
    }
    public void Datefilter(String time_data){
        //String dateString = "Wed, 18 Oct 2023 09:10:00 GMT";
        String dateString = time_data;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        try {
            Date date = dateFormat.parse(dateString); // 문자열을 Date 객체로 파싱
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년 MM월 dd일 ", Locale.KOREA);  // 필요에 따라 다른 형식으로 날짜를 출력
            String outputDateString = outputFormat.format(date);
            logDate = outputDateString;
        } catch (ParseException e) {
            e.printStackTrace();
            // 예외 처리 코드 추가
        }
    }
}