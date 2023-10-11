package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import java.util.Stack;

public class MainActivity3 extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> cer_cameraLauncher;
    ImageView imageView;
    String information;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_3_jtm);
        imageView = findViewById(R.id.imageView2);
        ImageButton back = findViewById(R.id.ibtn_Backlog);
        Button certified = findViewById(R.id.btn_cer);
        checkBox = findViewById(R.id.cb_MJ);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 현재 액티비티를 종료하고 이전 화면으로 돌아가기
            }
        });
        certified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cer_takePicture();}
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }

        // ActivityResultLauncher 초기화
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(imageBitmap);
                        }
                    }
                });
        cer_cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            //사진ocr하는곳으로 보내기 구현??
                            information = "테스트 123 나오면 확인버튼 클릭";
                            showInfoDialog(MainActivity3.this, "정보 확인", information);

                        }
                    }
                });
    }
    public void takePicture(View view1) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
    }
    public void cer_takePicture() {
        Intent cer_takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cer_takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cer_cameraLauncher.launch(cer_takePictureIntent);
        }
    }
    public void showInfoDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 사용자가 확인 버튼을 눌렀을 때 수행할 동작을 여기에 추가
                        Confirmation();
                    }
                })
                .setNeutralButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { Editing();}
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cancellation();
                    }
                })
                .show(); // 다이얼로그 표시
    }
    public void showEditableDialog(final Context context, String title, String initialValue, final OnValueEditedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_3_z_dialogedit, null);
        final EditText editTextValue = view.findViewById(R.id.editTextValue);
        editTextValue.setText(initialValue);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setTitle(title)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 사용자가 입력한 값을 가져옵니다.
                        String editedValue = editTextValue.getText().toString();

                        // 리스너를 통해 수정된 값을 전달합니다.
                        if (listener != null) {
                            listener.onValueEdited(editedValue);
                        }
                        Toast.makeText(getApplicationContext(), "완료하였습니다.", Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(true);
                    }
                })
                .setNegativeButton("취소", null) // 취소 버튼 설정
                .show(); // 다이얼로그 표시
    }
    public interface OnValueEditedListener {
        void onValueEdited(String editedValue);
    }
    private void Confirmation() {
        // 안내문자서 확인 클릭시
        // 예: 다음 화면으로 이동, 데이터 저장, 기타 작업 수행
        Toast.makeText(getApplicationContext(), "완료하였습니다.", Toast.LENGTH_SHORT).show();
        checkBox.setChecked(true);
    }
    private void Cancellation() {
        // 안내문자서 취소 클릭시
        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
        checkBox.setChecked(false);
    }
    private void Editing() {
        // 안내문자서 수정 클릭시
        showEditableDialog(this, "수정하기", information, new OnValueEditedListener() {
            @Override
            public void onValueEdited(String editedValue) {
                // 사용자가 수정한 값을 사용합니다.
                // editedValue 변수에 수정된 값이 들어있습니다.
                // 이곳에서 수정된 값을 사용하거나 처리할 수 있습니다.
            }
        });
    }
    //------------------------------------------------------------------------------------------------------------------------------

}