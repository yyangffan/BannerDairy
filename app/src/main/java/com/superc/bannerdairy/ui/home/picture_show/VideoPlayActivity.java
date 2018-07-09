package com.superc.bannerdairy.ui.home.picture_show;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayActivity extends AppCompatActivity {
    private JZVideoPlayerStandard mJZVideoPlayerStandard;
    public static final String DIZHI="DIZHI";
    public String dizhi="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        initView();
        initData();
    }

    public Intent callIntent(Context context,String dizhi) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(DIZHI,dizhi);
        return intent;
    }

    public void initView() {
        mJZVideoPlayerStandard = findViewById(R.id.videoplayer);
        dizhi=getIntent().getStringExtra(DIZHI);
    }

    public void initData() {
        Glide.with(this).load(ConnectUrl.REQUESTURL + dizhi).into(mJZVideoPlayerStandard.thumbImageView);
        mJZVideoPlayerStandard.setUp(ConnectUrl.REQUESTURL + dizhi, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        mJZVideoPlayerStandard.startVideo();
    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

}
