package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity6 extends AppCompatActivity {

    ImageButton ibtn_logout;
    ImageButton ibtn_Back;
    Button edit_Mypageinfo;
    TextView tv_name, tv_id, tv_pw, tv_grp, tv_pnum;
    String name, id, pw, grp, pnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_6_mypage);

        ibtn_logout = findViewById(R.id.ibtn_logout);
        ibtn_Back = findViewById(R.id.ibtn_ulcBack);
        //TableLayout tabLayout = findViewById(R.id.tabs);
        edit_Mypageinfo = findViewById(R.id.btn_mypage_edit);
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
        edit_Mypageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_name = findViewById(R.id.tv_mypage_rName); name = tv_name.getText().toString();
                tv_id = findViewById(R.id.tv_mypage_rName); id = tv_id.getText().toString();
                tv_pw = findViewById(R.id.tv_mypage_rName); pw = tv_pw.getText().toString();
                tv_grp = findViewById(R.id.tv_mypage_rName); grp = tv_grp.getText().toString();
                tv_pnum = findViewById(R.id.tv_mypage_rName); pnum = tv_pnum.getText().toString();
                showEditableDialog(this, name, id, pw, grp, pnum);
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
                        //    listener.onValues_Edited(name, idNumber, gender, institution);
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
        // 예: 다음 화면으로 이동, 데이터 저장, 기타 작업 수행
        Toast.makeText(getApplicationContext(), "완료하였습니다.", Toast.LENGTH_SHORT).show();
    }
    private void Cancellation() {
        // 안내문자서 취소 클릭시
        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
    }
}