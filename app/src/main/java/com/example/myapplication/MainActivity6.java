package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity6 extends AppCompatActivity {

    ImageButton ibtn_logout;
    ImageButton ibtn_Back;
    Button edit_Mypageinfo;
    TabItem Tab_myinfo;
    TextView tv_name, tv_id, tv_pw, tv_grp, tv_pnum;
    String name, id, pw, grp, pnum;
    String server_Result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_6_mypage);

        ibtn_logout = findViewById(R.id.ibtn_logout);
        ibtn_Back = findViewById(R.id.ibtn_ulcBack);
        //TableLayout tabLayout = findViewById(R.id.tabs);
        edit_Mypageinfo = findViewById(R.id.btn_mypage_edit);
        Tab_myinfo = findViewById(R.id.Tab_myinfo);
        ibtn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });
        ibtn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 현재 액티비티를 종료하고 이전 화면으로 돌아가기
            }
        });
        if (Tab_myinfo != null) {
            Tab_myinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "새로고침 완료", Toast.LENGTH_SHORT).show();
                    // 이곳에 클릭 이벤트 핸들러 코드를 추가하세요.
                }
            });
        } else {
            Log.e("MainActivity6", "TabItem is null. Check the ID in your XML layout.");
            Toast.makeText(getApplicationContext(), "탭버튼이 null", Toast.LENGTH_SHORT).show();
        }
        edit_Mypageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditableDialog(this::onClick, name, id, pw, grp, pnum);
            }
        });
    }
    public void showEditableDialog(View.OnClickListener context, String edName, String edID, //v2
                                   String edPW, String edGroup, String edPNum) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewV1 = inflater.inflate(R.layout.activity_6_x1_mypageedit, null);
        EditText E_Name = viewV1.findViewById(R.id.etv_mypage_ename);
        EditText E_ID = viewV1.findViewById(R.id.etv_mypage_eID);
        EditText E_PW = viewV1.findViewById(R.id.etv_mypage_ePW);
        EditText E_Grp = viewV1.findViewById(R.id.etv_mypage_eGroup);
        EditText E_PNum = viewV1.findViewById(R.id.etv_mypage_ePNum);

        E_Name.setText(edName);
        E_ID.setText(edID);
        E_PW.setText(edPW);
        E_Grp.setText(edGroup);
        E_PNum.setText(edPNum);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewV1)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 사용자가 수정한 값을 가져옵니다.
                        //name = etName.getText().toString();

                        // 리스너를 통해 수정된 값을 전달합니다.
                        //if (listener != null) {
                        //    listener.onValues_Edited(name, id, pw, grp, pnum);
                        //}

                        Confirmation();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Cancellation();
                    }
                })
                .show(); // 다이얼로그 표시
    }
    public interface OnValuesEditedListener {
        void onValues_Edited(String editedName, String editedId, String editedPW, String editedGroup, String editedPnum);
    }
    private void Confirmation() {
        // 안내문자서 확인 클릭시
        Toast.makeText(getApplicationContext(), "완료하였습니다.", Toast.LENGTH_SHORT).show();
    }
    private void Cancellation() {
        // 안내문자서 취소 클릭시
        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
    }
    public void Find_info() { // 서버로 보내기

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name",name)
                .addFormDataPart("id",id)
                .build();

        Request request = new Request.Builder()
                .url("http://15.164.120.162:5000//Sign_up") // 여기 sign_up 말고 딴거 넣어야됨
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
                        server_Result = result;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //아니 서버서 받은 결과 필터링 해야되나
                                //showInfoDialog(MainActivity3.this,ocr_result);
                            }
                        });
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
    public void Send2_edinfo(File file) { // 서버로 보내기(수정시)

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name",name) //이름
                .addFormDataPart("id",id) //아이디
                .addFormDataPart("pw",pw) //비번
                .addFormDataPart("group",grp) //그룹
                .addFormDataPart("pnum",pnum) //폰넘
                .build();

        Request request = new Request.Builder()
                .url("http://15.164.120.162:5000//Sign_up") // 여기 sign_up 말고 딴거 넣어야됨
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

                        Log.d("JSON Response: ", "status: " + status);
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