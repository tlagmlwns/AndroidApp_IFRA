package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.*;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.json.JSONObject;

public class MainActivity3 extends AppCompatActivity {
    Single_ton_data mydata = Single_ton_data.getInstance();
    String directoryPath;
    File directory;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> cer_cameraLauncher;
    private String ocr_result;
    ImageView imageView;
    ImageButton back;
    Button certified;
    String information;
    CheckBox checkBox;
    Context context;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//다크모드 삭제
        setContentView(R.layout.activity_3_jtm);

        imageView = findViewById(R.id.imageView2);
        back = findViewById(R.id.ibtn_Backlog);
        certified = findViewById(R.id.btn_cer);
        checkBox = findViewById(R.id.cb_MJ);
        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("로딩 중...");
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
                            directoryPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
                            directory = new File(directoryPath);

                            if (!directory.exists()) {
                                if (!directory.mkdirs()) {
                                    Log.e("Directory Creation Error", "Failed to create directory.");
                                }
                            }

                            // 이미지를 파일로 저장
                            File internalFile = new File(directory, "my_image.jpg");
                            try {
                                FileOutputStream fos = new FileOutputStream(internalFile);
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.flush();
                                fos.close();
                                Log.e("image storage", "Success: " + internalFile.getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("image storage", "Fail: " + e.getMessage());
                            }

                            // 서버로 이미지를 업로드
                            send2Server(internalFile);
                            //information = ocr_result;
                            //showInfoDialog(MainActivity3.this, "정보 확인", information);
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
    public void send2Server(File file) {
        MediaType MEDIA_TYPE = MediaType.parse("image/jpeg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE, file))
                .build();

        Request request = new Request.Builder()
                .url("http://15.164.120.162:5000/ocr_test")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        progressDialog.show();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    progressDialog.dismiss();

                    try {
                        JSONObject json = new JSONObject(responseBody);

                        // 여기에서 JSON 데이터를 처리하고 원하는 정보 추출
                        String status = json.getString("status");
                        String result = json.getString("result");

                        Log.d("JSON Response: ", "status: " + status);
                        Log.d("JSON Response: ", "result: " + result);
                        ocr_result = result;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showInfoDialog(MainActivity3.this, "서버에서 받은 데이터", ocr_result);
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

}