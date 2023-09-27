package com.m0103.tp14petflex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.data.BoardData;
import com.m0103.tp14petflex.databinding.RecyclerBoardBinding;
import com.m0103.tp14petflex.network.RetrofitHelper;
import com.m0103.tp14petflex.network.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.VH> {
    Context context;
    ArrayList<BoardData> boardDataArrayList;
    int isFav=0;


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

        holder.itemView.setOnClickListener(view -> createDialog(boardData.nickname, boardData.date,boardData.pet_name,
                boardData.pet_age,boardData.pet_breed,url));

        countFav(holder, boardData.board_no);

        setIsFav(holder.itemView,holder, boardData.board_no);

        holder.binding.boardBtnFav.setOnClickListener(view -> {
            //TODO:좋아요 테스트 끝나면 if문 걸기
//            if(G.login==0) {
//                Toast.makeText(context, "로그인이 필요한 기능이에요", Toast.LENGTH_SHORT).show();
//                return;
//            }
            clickBtnFav(holder.itemView, (Integer)holder.itemView.getTag(),holder, boardData.board_no);
        });

    }

    void setIsFav(View itemView,VH holder, String board_no){ //이전 좋아요 표시
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<String[]> call=retrofitService.setIsFav(G.nickname);
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                String[] s=response.body();
                if((s.length==0)) return;

                for(int i = 0; i<s.length; i++){
                    if(s[i].equals(board_no)) {
                        holder.binding.boardBtnFav.setBackgroundResource(R.drawable.ic_favorite);
                        itemView.setTag(1);
                    }
                }
            }
            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
            }
        });
    }

    void countFav(VH holder, String board_no){ //좋아요 수 카운트
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<String> call=retrofitService.countFavorite(board_no);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s=response.body();

                if(s.equals("")) holder.binding.boardTvCount.setText("0");
                else holder.binding.boardTvCount.setText(s);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    void clickBtnFav(View itemView,int i,VH holder,String board_no){ //좋아요 기능
        if(i==0){ //좋아요
            Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
            RetrofitService retrofitService=retrofit.create(RetrofitService.class);
            Call<String> call=retrofitService.favorite(board_no, G.nickname);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String s=response.body();
                    if(s.equals("true")) holder.binding.boardBtnFav.setBackgroundResource(R.drawable.ic_favorite);
                    countFav(holder, board_no);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
            i++;
        } else if(i==1){ //짝수 : 좋아요 취소
            Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
            RetrofitService retrofitService=retrofit.create(RetrofitService.class);
            Call<String> call=retrofitService.favoriteCancel(board_no, G.nickname);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String s=response.body();
                    if(s.equals("true")) holder.binding.boardBtnFav.setBackgroundResource(R.drawable.ic_favorite_border);
                    countFav(holder, board_no);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
            i--;
        }
        itemView.setTag(i);
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
            itemView.setTag(0);
        }
    }

    void createDialog(String nickname1,String date1, String pet_name, String pet_age, String pet_breed, String url){
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

        nickname.setText(nickname1+"님");
        date.setText(date1);
        petName.setText("ʚ "+pet_name+" ɞ");
        petBreed.setText(pet_breed);
        if(pet_age.equals("")) petAge.setText(null);
        else petAge.setText(pet_age+"살");

        Glide.with(context).load(url).into(iv);
    }


}
