package com.superc.bannerdairy.ui.home.picture_show;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.superc.bannerdairy.R;
import com.superc.cframework.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.superc.bannerdairy.R.id.money;

/**
 * Created by user on 2018/1/19.
 */

public class MyWindowDialog extends Dialog implements View.OnClickListener{
    private ConvenientBanner banner;
    private TextView mTextView;
    private ImageView mtv_one;
    private TextView mtv_two;
    private LinearLayout mll;
    private List<Map<String, Object>> bannerList;
    private int position=0;



    public MyWindowDialog(@NonNull Context context, List<Map<String, Object>> bannerList,int position) {
        super(context);
        this.position=position;
        this.bannerList = bannerList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.window_dialog, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x0000ffff));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        banner = findViewById(R.id.banner);
        mTextView = findViewById(R.id.num);
        mtv_one = findViewById(R.id.tobackOne);
        mtv_two = findViewById(R.id.tobackTwo);
        mll=findViewById(R.id.window_ll);
        mtv_one.setOnClickListener(this);
        mtv_two.setOnClickListener(this);
        mll.setOnClickListener(this);
        initData();
    }

    public void initData() {
        final List list = new ArrayList();
        if (bannerList.size() > 1) {
            banner.startTurning(1000 * 10000);
        }
        for (int a = 0; a < bannerList.size(); a++) {
            Map<String, Object> map = bannerList.get(a);
            banner.setBackground(null);
            list.add(map.get("image"));
        }
        mTextView.setText((position+1)+"/" + list.size());
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public MyImageHolderView createHolder() {
                return new MyImageHolderView();
            }
        }, list).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
        .setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.e("点击到了","第"+position+"个");
                MyWindowDialog.this.dismiss();
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTextView.setText((position + 1) + "/" + list.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        banner.setcurrentitem(position);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tobackOne:
            case R.id.tobackTwo:
            case R.id.window_ll:
                this.dismiss();
                break;


        }
    }
}
