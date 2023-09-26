package com.m0103.tp14petflex.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.data.BoardData;
import com.m0103.tp14petflex.databinding.RecyclerBoardBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.VH> {
    Context context;
    ArrayList<BoardData> boardDataArrayList;


    public BoardRecyclerAdapter(Context context, ArrayList<BoardData> boardDataArrayList) {
        this.context = context;
        this.boardDataArrayList = boardDataArrayList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_board,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BoardData boardData=boardDataArrayList.get(position);

        holder.binding.boardTvName.setText(boardData.pet_name);

        String url="http://petflex.dothome.co.kr/board/"+boardData.img;
        Glide.with(context).load(url).into(holder.binding.boardIv);

        holder.itemView.setOnClickListener(view -> { //클릭 시 다이얼로그
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setView(R.layout.dialog_board);
            AlertDialog dialog=builder.create();
            dialog.show();

            TextView nickname=dialog.findViewById(R.id.dialog_nickname);
            TextView petName=dialog.findViewById(R.id.dialog_pet_name);
            TextView petBreed=dialog.findViewById(R.id.dialog_pet_breed);
            TextView petAge=dialog.findViewById(R.id.dialog_pet_age);
            TextView date=dialog.findViewById(R.id.dialog_date);
            ImageView iv=dialog.findViewById(R.id.dialog_iv);

            nickname.setText(boardData.nickname+"님의 자랑");
            date.setText(boardData.date);
            petName.setText("ʚ "+boardData.pet_name+" ɞ");
            petBreed.setText(boardData.pet_breed);
            if(boardData.pet_age.equals("")) petAge.setText(null);
            else petAge.setText(boardData.pet_age+"살");

            Glide.with(context).load(url).into(iv);
        });

    }

    @Override
    public int getItemCount() {
        return boardDataArrayList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerBoardBinding binding;
        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerBoardBinding.bind(itemView);

        }
    }
}
