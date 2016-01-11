package com.huangyinghao.playermp3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.domain.MusicInfo;
import com.huangyinghao.playermp3.domain.SingerInfo;
import com.huangyinghao.playermp3.utils.ToolUtils;

import java.util.List;

/**
 * Created by deny on 2016/1/2.
 */
public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.MyViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<SingerInfo> mSingerInfos;
    private final LayoutInflater inflater;
    private long alubmId;

//    private List<MusicInfo> musicInfos;
//    private List<String> singers;

    public interface OnClickListener{
        void onClick(View view , long pos);
    }
    private OnClickListener listener;

    public OnClickListener getListener() {
        return listener;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public SingerAdapter(Context context , List<SingerInfo> singerInfos){
        mContext = context ;
        mSingerInfos = singerInfos;

        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singer_recycleview , parent , false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        long artId = mSingerInfos.get(position).getArtId();

        List<MusicInfo> musicInfos = MusicInfo.where("artid = ?", String.valueOf(artId)).find(MusicInfo.class);

        holder.singer.setText(musicInfos.get(0).getSinger());

        int num = musicInfos.size();
        holder.songNum.setText(String.valueOf(num) + "首歌");
        alubmId = musicInfos.get(0).getAlbun_id();
        ToolUtils.loadCirPic(mContext, alubmId, holder.singerImg);

        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(artId);

    }


    @Override
    public void onClick(View v) {
        if(getListener() != null){
            Long alubmIdTag = (Long) v.getTag();
            getListener().onClick(v , alubmIdTag);
        }
    }

    @Override
    public int getItemCount() {
        return mSingerInfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView singer;
        private TextView songNum;
        private ImageView singerImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            singerImg = (ImageView) itemView.findViewById(R.id.singer_img);
            singer = (TextView) itemView.findViewById(R.id.singer_singer);
            songNum = (TextView) itemView.findViewById(R.id.singer_number);
        }
    }
}
