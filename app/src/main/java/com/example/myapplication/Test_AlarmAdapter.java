package com.example.myapplication;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        //작성한 list_iem.xml 를 생성하는 부분이라고 생각하시면 됩니다.
        return new BoardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_9_z_warning_no, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        // ViewHolder 가 재활용 될 때 사용되는 메소드
        Test_Alarm data = dataList.get(position);
        holder.filename.setText(data.getFilename());
        holder.filedate.setText(data.getFiledate());
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
        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            filename = itemView.findViewById(R.id.tv_mm_fnR);
            filedate = itemView.findViewById(R.id.tv_mm_dateR);
        }
    }
}

