package com.m0103.tp14petflex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.data.BoardData;
import com.m0103.tp14petflex.databinding.RecyclerHomeBinding;
import com.m0103.tp14petflex.databinding.RecyclerHomeFirstBinding;

import java.time.LocalDate;
import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<BoardData> boardDataArrayList;
    int lastMonth= LocalDate.now().minusMonths(1).getMonthValue();

    public HomeRecyclerAdapter(Context context, ArrayList<BoardData> boardDataArrayList) {
        this.context = context;
        this.boardDataArrayList = boardDataArrayList;
    }

    int i=0;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(i==0){
            View itemview=LayoutInflater.from(context).inflate(R.layout.recycler_home_first,parent,false);
            return new VH_first(itemview);
        }else {
            View itemview=LayoutInflater.from(context).inflate(R.layout.recycler_home,parent,false);
            return new VH(itemview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BoardData boardData=boardDataArrayList.get(position);
        String url="http://petflex.dothome.co.kr/board/"+boardData.img;

        if(i==0){
            VH_first vhFirst= (VH_first) holder;
            vhFirst.firstBinding.homeTv1Name.setText("< "+boardData.pet_name+" >");
            vhFirst.firstBinding.homeTv1Fav.setText(lastMonth+"월에 받은 좋아요 "+boardData.count_last_month+"개");
            Glide.with(context).load(url).into(vhFirst.firstBinding.homeIv1);
            i=1;
        }else {
            VH vh= (VH) holder;
            vh.binding.homeTv2Name.setText(boardData.pet_name);
            vh.binding.homeTv2Fav.setText("좋아요 "+boardData.count_last_month+"개");
            Glide.with(context).load(url).into(vh.binding.homeIv2);
        }

        holder.itemView.setOnClickListener(view -> BoardRecyclerAdapter.createDialog(context,boardData.nickname,
                boardData.date,boardData.pet_name, boardData.pet_age, boardData.pet_breed, url));

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class VH_first extends RecyclerView.ViewHolder{
        RecyclerHomeFirstBinding firstBinding;
        public VH_first(@NonNull View itemView) {
            super(itemView);
            firstBinding=RecyclerHomeFirstBinding.bind(itemView);
        }
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerHomeBinding binding;
        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerHomeBinding.bind(itemView);
        }
    }
}
