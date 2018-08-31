package com.acode.player.listplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.acode.player.R;
import com.acode.player.lib.AcodePlayerView;
import com.acode.player.lib.bean.PlayerBean;
import com.acode.player.lib.data.Data;
import com.acode.player.lib.listener.AcodePlayerStateListener;
import com.acode.player.lib.utils.Dlog;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * user:yangtao
 * date:2018/6/61739
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class ListPlayerAdapter extends RecyclerView.Adapter {
    private OnItemClickListener onItemClickListener;
    private AcodePlayerView acodePlayerView;
    private Context context;
    private ArrayList<PlayerBean> playerBeans;

    public ListPlayerAdapter(Context context) {
        this.context = context;
        playerBeans = new ArrayList<>();
        playerBeans.addAll(Data.getPlayerBeans());
        playerBeans.addAll(Data.getPlayerBeans());
    }

    public ListPlayerAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        final TestViewHolder testViewHolder = (TestViewHolder) holder;
        testViewHolder.iv.setVisibility(View.VISIBLE);
        testViewHolder.ll.setVisibility(View.INVISIBLE);
        testViewHolder.iv.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TestViewHolder) {
            final TestViewHolder testViewHolder = (TestViewHolder) holder;
            testViewHolder.bindData(playerBeans.get(position));
            testViewHolder.iv.setVisibility(View.VISIBLE);
            testViewHolder.ll.setVisibility(View.INVISIBLE);
            testViewHolder.iv.setImageResource(R.mipmap.ic_launcher);
            if (playerBeans.get(position).getMs() > 0) {
                testViewHolder.cv.setVisibility(View.VISIBLE);
            } else {
                testViewHolder.cv.setVisibility(View.GONE);
            }
            testViewHolder.iv.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                    if (acodePlayerView == null) {
                        acodePlayerView = new AcodePlayerView(context);
                    }
                    //找到播放器的爹
                    LinearLayout linearLayout = (LinearLayout) acodePlayerView.getParent();
                    if (linearLayout != null) {
                        //然后移除
                        linearLayout.removeAllViews();
                    }
                    //给他添加新爹
                    testViewHolder.ll.addView(acodePlayerView);
                    acodePlayerView.pausePlayer();
                    acodePlayerView.readyPlayer(playerBeans.get(position));
                    testViewHolder.iv.setVisibility(View.INVISIBLE);
                    testViewHolder.ll.setVisibility(View.VISIBLE);
                    acodePlayerView.setAcodePlayerStateListener(new AcodePlayerStateListener() {
                        @Override
                        public void playerRuning(PlayerBean playerBean) {
                            playerBeans.set(position, playerBean);
                            Dlog.i(ListPlayerAdapter.class, "播放器内存地址：" + acodePlayerView);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        int pos = holder.getAdapterPosition();
//            Log.d("MyViewHolder", String.format("mCvCountdownView %s is attachedToWindow", pos));

        PlayerBean itemInfo = playerBeans.get(pos);

        ((TestViewHolder) holder).refreshTime(itemInfo.getMs() - System.currentTimeMillis());
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((TestViewHolder) holder).getCvCountdownView().stop();
    }


    @Override
    public int getItemCount() {
        return playerBeans.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        LinearLayout ll;
        CountdownView cv;
        PlayerBean mItemInfo;

        public TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
            iv = itemView.findViewById(R.id.iv);
            ll = itemView.findViewById(R.id.ll);
            cv = itemView.findViewById(R.id.cv);
        }

        public void bindData(PlayerBean itemInfo) {
            mItemInfo = itemInfo;
            refreshTime(mItemInfo.getMs() - System.currentTimeMillis());
        }

        public CountdownView getCvCountdownView() {
            return cv;
        }

        public void refreshTime(long leftTime) {
            if (leftTime > 0) {
                cv.start(leftTime);
            } else {
                cv.stop();
                cv.allShowZero();
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AcodePlayerView getApv() {
        return acodePlayerView;
    }
}
