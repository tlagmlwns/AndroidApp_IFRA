package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity8 extends AppCompatActivity {
    ImageButton mpBack;
    EditText Year;
    Spinner Month, dayOM;
    ImageButton ALSearch;
    String yyyy, mm, dom;
    String in_T, out_T, statE, confidencE, user_iD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_8_);

        mpBack = findViewById(R.id.ibtn_mpBack);
        Year = findViewById(R.id.etv_mm_YEAR);
        Month = findViewById(R.id.sp_mm_Month);
        dayOM = findViewById(R.id.sp_mm_dOM);
        ALSearch = findViewById(R.id.ibtn_mm_search);
        mpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();}
        });
        String[] mms = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        ArrayAdapter<String> mms_adapter  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mms);
        Month.setAdapter(mms_adapter);
        Month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mm = mms[position].toString(); // 사용자가 선택한 항목의 텍스트 가져오기
                Toast.makeText(getApplicationContext(), "선택된 항목: " + mm, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                mm = ""; // 또는 다른 기본값 설정
                Toast.makeText(getApplicationContext(), "month를 선택하시오", Toast.LENGTH_SHORT).show();
            }
        });
        String[] doms = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                         "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                         "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                         "31"};
        ArrayAdapter<String> doms_adapter  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doms);
        dayOM.setAdapter(doms_adapter);
        dayOM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                dom = doms[position].toString(); // 사용자가 선택한 항목의 텍스트 가져오기
                Toast.makeText(getApplicationContext(), "선택된 항목: " + dom, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "doy of month를 선택하시오", Toast.LENGTH_SHORT).show();
            }
        });
        ALSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yyyy = Year.getText().toString();
                All_log(yyyy, mm, dom);
            }
        });
    }

    public void All_log (String year, String month, String dayofmonth){
        String date = year+"-"+month+"-"+dayofmonth;
        Log.d("JSON Response: ", "date: " + date); //1. 이거 까진 잘 들어가는데
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("date",date)
                .build();

        Request request = new Request.Builder()
                .url("http://15.164.120.162:5000//all_log")
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
                        String inner_time = json.getString("inner_time"); //2. 이건 안나오네
                        String outter_time = json.getString("outter_time");
                        String state = json.getString("state");
                        String confidence = json.getString("confidence");
                        String user_id = json.getString("user_id");

                        Log.d("JSON Response: ", "status: " + status);
                        Log.d("JSON Response: ", "inner_time: " + inner_time);
                        Log.d("JSON Response: ", "outter_time: " + outter_time);
                        Log.d("JSON Response: ", "state: " + state);
                        Log.d("JSON Response: ", "confidence: " + confidence);
                        Log.d("JSON Response: ", "user_id: " + user_id);

                        if (inner_time == null) {in_T = "";} else{ in_T = inner_time;}
                        if (outter_time == null) {out_T = "";} else{ out_T = outter_time;}
                        if (state == null) {statE = "";} else{ statE = state;}
                        if (confidence == null) {confidencE = "";} else{ confidencE = confidence;}
                        if (user_id == null) {user_iD = "";} else{ user_iD = user_id;}

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // 응답이 성공적이지 않은 경우에 대한 처리
                    Log.e("Response Error", "Response Code: " + response.code());
                }
            }
        });
    }
}