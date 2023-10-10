package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {
    Single_ton_data mydata = Single_ton_data.getInstance();
    Button Login;
    EditText id;
    EditText pw;
    TextView Sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_2_login);

        id = findViewById(R.id.InputID);
        pw = findViewById(R.id.InputPassword);
        Login = findViewById(R.id.btn_login);
        Sign_up = findViewById(R.id.link3);
        Sign_up.setOnClickListener(new View.OnClickListener() {//회원가입
            @Override
            public void onClick(View view) {
                Intent jtm=new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(jtm);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {//로그인시 메인페이지(임시)
            @Override
            public void onClick(View view) {
                String uid = id.getText().toString();
                String upw=pw.getText().toString();
                url_request(uid,upw);
            }
        });
    }
    public void onBackPressed() { //뒤로가기눌렀을때 선택지 주기
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말로 앱을 종료하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();// 사용자가 확인을 누를 경우 앱을 완전히 종료
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();// 사용자가 취소를 누를 경우 다이얼로그를 닫음
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void url_request(String u_id,String u_pw){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://15.164.120.162:5000/login?id=" + u_id + "&pw=" + u_pw,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // JSON 데이터를 파싱하여 필요한 작업을 수행합니다.
                            String status = response.getString("status");
                            Log.e("response","state :"+status);
                            if ("success".equals(status)) {
                                // 로그인 성공 처리
                                Log.e("event","Login Success");
                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                mydata.setUser_id(response.getString("user_id"));
                                mydata.setUser_pass(response.getString("user_pass"));
                                mydata.setUser_group(response.getString("user_group"));
                                Log.e("userid","user_id :"+mydata.getUser_id());
                                Log.e("userpass","user_pass"+mydata.getUser_pass());
                                Log.e("usergroup","user_group"+mydata.getUser_group());
                                String userId = id.getText().toString();
                                if (userId.startsWith("m_")) {
                                    Intent user = new Intent(getApplicationContext(), MainActivity5.class); //5:관리자
                                    startActivity(user);
                                }
                                else {
                                    Intent manager = new Intent(getApplicationContext(), MainActivity4.class); //4:사용자
                                    startActivity(manager);
                                }
                            } else {
                                Log.e("event","Login Fail");
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                // 로그인 실패 처리
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 요청이 실패한 경우 처리
                        error.printStackTrace();
                    }
                }
        );

// Volley 요청 큐를 초기화하고 요청을 큐에 추가
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}