package com.superc.bannerdairy.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.DefaultWebClient;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiaoTianFragment extends Fragment {
    protected AgentWeb mAgentWeb;
    String dizhi = "http://qizhi.linghangnc.com/index/im?user_id=31254";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liao_tian, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(ConnectUrl.LIAOTIAN + MyApplication.getUser().getUser_id()); //WebView载入该url地址的页面并显示。
        AgentWebConfig.debug();
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);

    }
    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();//恢复
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }

    private void initWeb() {
//       /* 设置支持Js,必须设置的,不然网页基本上不能看 */
//        mWebView.getSettings().setJavaScriptEnabled(true);
//       /* 设置缓存模式,我这里使用的默认,不做多讲解 */
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//       /* 设置为true表示支持使用js打开新的窗口 */
//       mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//       /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
//       mWebView.getSettings().setDomStorageEnabled(true);
//        /* 设置为使用webview推荐的窗口 */
//         mWebView.getSettings().setUseWideViewPort(true);
//        /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        /* HTML5的地理位置服务,设置为true,启用地理定位 */
//        mWebView.getSettings().setGeolocationEnabled(true);
//        /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
//        mWebView.getSettings().setBuiltInZoomControls(false);
//        /* 提高网页渲染的优先级 */
//        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        /* 设置显示水平滚动条,就是网页右边的滚动条.我这里设置的不显示 */
//        mWebView.setHorizontalScrollBarEnabled(false);
//        /* 指定垂直滚动条是否有叠加样式 */
//        mWebView.setVerticalScrollbarOverlay(true);
//        /* 设置滚动条的样式 */
//        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        /* 这个不用说了,重写WebChromeClient监听网页加载的进度,从而实现进度条 */
//        mWebView.setWebChromeClient(new WebChromeClient());
        /* 同上,重写WebViewClient可以监听网页的跳转和资源加载等等... */
//        mWebView.setWebViewClient(new WebViewClient());
//        if (MyApplication.getUser().user_id == null) {
//            ShowRemindDialog.getInstance().gotoLogin(LiaoTianFragment.this.getActivity());
//        } else {
//            mWebView.loadUrl(ConnectUrl.LIAOTIAN + MyApplication.getUser().getUser_id());
//        }
//        Log.e("user_id",MyApplication.getUser().getUser_id()==null?"":MyApplication.getUser().getUser_id());
    }

}
