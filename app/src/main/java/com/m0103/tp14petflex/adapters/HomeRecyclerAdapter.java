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
    String type;

    public HomeRecyclerAdapter(Context context, ArrayList<BoardData> boardDataArrayList, String type) {
        this.context = context;
        this.boardDataArrayList = boardDataArrayList;
        this.type = type;
    }

    final int first=1;
    final int other=2;
    String s=null;

    @Override
    public int getItemViewType(int position){
        if (boardDataArrayList.size()<10 && position>=boardDataArrayList.size()) return other;

        else if(0==boardDataArrayList.indexOf(boardDataArrayList.get(position))) {
            s=boardDataArrayList.get(position).board_no;
            return first;
        }
        else return other;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==first){
            View itemview=LayoutInflater.from(context).inflate(R.layout.recycler_home_first,parent,false);
            return new VH_first(itemview);
        }else {
            View itemview=LayoutInflater.from(context).inflate(R.layout.recycler_home,parent,false);
            return new VH(itemview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(boardDataArrayList.size()<10 && position>=boardDataArrayList.size()) return;
        BoardData boardData=boardDataArrayList.get(position);
        String url="http://petflex.dothome.co.kr/board/"+boardData.img;

        if(s.equals(boardData.board_no)){
            VH_first vhFirst= (VH_first) holder;
            vhFirst.firstBinding.homeTv1Name.setText("< "+boardData.pet_name+" >");

            if(type.equals("lastMonth")) vhFirst.firstBinding.homeTv1Fav.setText(boardData.count_last_month+"개");
            else if(type.equals("totalFav")) vhFirst.firstBinding.homeTv1Fav.setText(boardData.count_fav+"개");
            else if(type.equals("lastWeek")) vhFirst.firstBinding.homeTv1Fav.setText(boardData.count_last_week+"개");

            Glide.with(context).load(url).into(vhFirst.firstBinding.homeIv1);
        }else {
            VH vh= (VH) holder;
            vh.binding.homeTv2Name.setText(boardData.pet_name);

            if(type.equals("lastMonth")) vh.binding.homeTv2Fav.setText(boardData.count_last_month+"개");
            else if(type.equals("totalFav")) vh.binding.homeTv2Fav.setText(boardData.count_fav+"개");
            else if(type.equals("lastWeek")) vh.binding.homeTv2Fav.setText(boardData.count_last_week+"개");

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
