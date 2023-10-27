package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Test_AlarmAdapter extends RecyclerView.Adapter<Test_AlarmAdapter.BoardViewHolder>{
    private List<Test_Alarm> dataList; //데이터 리스트
    private Context resources;
    public Test_AlarmAdapter(List<Test_Alarm> dataList){ this.dataList = dataList;}

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ViewHolder 객체 생성 후 리턴.
        return new BoardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_9_y_warning_no, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        // ViewHolder 가 재활용 될 때 사용되는 메소드
        Test_Alarm data = dataList.get(position);
        holder.filename.setText(data.getFilename());
        holder.filedate.setText(data.getFiledate());
        holder.Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼이 클릭되었을 때 실행할 코드 작성
                // 새 다이얼로그를 띄워서 다른 레이아웃을 포함시킬 수 있습니다.
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
                View dialogView = inflater.inflate(R.layout.activity_9_z_, null); // 새로운 레이아웃을 인플레이트합니다.

                // 다이얼로그 레이아웃에 포함된 위젯들에 대한 작업을 할 수 있습니다.
                // textView.setText("다이얼로그 내용 설정"); // 다이얼로그 내용을 동적으로 설정할 수 있습니다.
                ImageView imageView = dialogView.findViewById(R.id.im_NF); // 이미지뷰를 찾습니다.
                imageView.setImageResource(R.drawable.lucky7);
                builder.setView(dialogView)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 확인 버튼을 눌렀을 때의 동작
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() { return dataList.size();} // 전체 데이터의 개수 조회

    public Context getResources() {
        return resources;
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder 에 필요한 데이터들
        private TextView filename;
        private TextView filedate;
        private ImageButton Picture;
        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            filename = itemView.findViewById(R.id.tv_mm_fnR);
            filedate = itemView.findViewById(R.id.tv_mm_dateR);
            Picture = itemView.findViewById(R.id.ibtn_Pic);
        }
    }
}

