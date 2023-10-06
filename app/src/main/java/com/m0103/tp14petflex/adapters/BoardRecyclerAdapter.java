package com.m0103.tp14petflex.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.activities.MainActivity;
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
    String type="nothing";

    public BoardRecyclerAdapter(Context context, ArrayList<BoardData> boardDataArrayList) {
        this.context = context;
        this.boardDataArrayList = boardDataArrayList;
    }

    public BoardRecyclerAdapter(Context context, ArrayList<BoardData> boardDataArrayList, String type) {
        this.context = context;
        this.boardDataArrayList = boardDataArrayList;
        this.type = type;
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

        holder.itemView.setOnClickListener(view -> createDialog(context, boardData.nickname, boardData.date,boardData.pet_name,
                boardData.pet_age,boardData.pet_breed,url));

        countFav(holder, boardData.board_no);

        setIsFav(holder.itemView,holder, boardData.board_no);

        holder.binding.boardBtnFav.setOnClickListener(view -> {
            if(G.login==0) {
                Toast.makeText(context, "로그인이 필요한 기능이에요", Toast.LENGTH_SHORT).show();
                return;
            }
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
                        break;
                    }else holder.binding.boardBtnFav.setBackgroundResource(R.drawable.ic_favorite_border);
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
        } else if(i==1){ //좋아요 취소
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


    class VH extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        RecyclerBoardBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerBoardBinding.bind(itemView);
            itemView.setTag(0);

            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if(type.equals("myPost") || G.nickname.equals("개발자")){ //삭제 버튼 : 내 게시물보기 탭, 개발자전용
                MenuItem delete= contextMenu.add("삭제");

                //delete 클릭 이벤트
                delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        String itemBoardNo=boardDataArrayList.get(getAdapterPosition()).board_no;
                        boardDataArrayList.remove(getAdapterPosition());

                        Retrofit retrofit = RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
                        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                        Call<String> call1 = retrofitService.deleteMyPost(itemBoardNo);
                        call1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String s=response.body();

                                if(s.equals("true")) Toast.makeText(context, "게시물이 삭제되었어요", Toast.LENGTH_SHORT).show();
                                else Toast.makeText(context, "오류가 발생했어요 다시 시도해주세요", Toast.LENGTH_SHORT).show();

                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(),boardDataArrayList.size());
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        return true;
                    }
                });
            }//delete if문

            if(type.equals("nothing")){ //신고 버튼 : 최신순, 좋아요순 구경 탭
                MenuItem report= contextMenu.add("신고");

                //신고 버튼 클릭 이벤트
                report.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        String itemBoardNo=boardDataArrayList.get(getAdapterPosition()).board_no;

                        Retrofit retrofit = RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
                        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                        Call<String> call1 = retrofitService.report(itemBoardNo);
                        call1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String s=response.body();

                                if(s.equals("true")) Toast.makeText(context, "신고 완료!\n빠른 검토 후 조치하겠습니다", Toast.LENGTH_SHORT).show();
                                else Toast.makeText(context, "오류가 발생했어요 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        return true;
                    }
                });
            }//report if문

            if(type.equals("report")){ //신고 초기화 버튼 : 개발자 전용
                MenuItem reportReset= contextMenu.add("신고 초기화");

                //신고 초기화 버튼 클릭 이벤트
                reportReset.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        String itemBoardNo=boardDataArrayList.get(getAdapterPosition()).board_no;
                        boardDataArrayList.remove(getAdapterPosition());

                        Retrofit retrofit = RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
                        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                        Call<String> call1 = retrofitService.reportReset(itemBoardNo);
                        call1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String s=response.body();

                                if(s.equals("true")) Toast.makeText(context, "초기화 완료", Toast.LENGTH_SHORT).show();
                                else Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(),boardDataArrayList.size());
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });

                        return true;
                    }
                });

            }

        }//onCreateContextMenu

    }//VH




    public static void createDialog(Context context,String nickname1,String date1, String pet_name, String pet_age, String pet_breed, String url){
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
