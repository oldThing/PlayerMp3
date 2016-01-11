package com.huangyinghao.playermp3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.domain.MusicInfo;

import java.util.List;

/**
 * Created by deny on 2015/12/24.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements View.OnClickListener {

    private Context mContext;
    List<MusicInfo> musics;
    private LayoutInflater inflater;


    public interface OnClickListener{
        void onClick(View view , int pos);
    }

    private OnClickListener listener;

    public OnClickListener getListener() {
        return listener;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public SongAdapter(Context pContext , List<MusicInfo> musics){
        mContext = pContext;
        this.musics = musics;
        inflater = LayoutInflater.from(pContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_recycleview_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemView.setTag(position);
        //设置点击事件
        holder.itemView.setOnClickListener(this);

        holder.songName.setText(musics.get(position).getName());
        holder.singer.setText(musics.get(position).getSinger());
        holder.songNumber.setText(String.valueOf(position + 1));

    }

    @Override
    public int getItemCount() {
        return musics.size();
    }


    @Override
    public void onClick(View v) {
        if(listener != null) {
            int position = (int) v.getTag();
            listener.onClick(v , position);
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView songNumber;
        private TextView songName;
        private final TextView singer;

        public MyViewHolder(View itemView) {
            super(itemView);
            songNumber = (TextView) itemView.findViewById(R.id.song_number);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            singer = (TextView) itemView.findViewById(R.id.song_singer);
        }
    }
}
