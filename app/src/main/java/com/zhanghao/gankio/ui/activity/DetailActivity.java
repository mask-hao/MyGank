package com.zhanghao.gankio.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.listener.WebViewScrollListener;
import com.zhanghao.gankio.ui.widget.MyScrollWebView;
import com.zhanghao.gankio.util.http.NetWorkUtil;

import butterknife.BindView;

/**
 * Created by zhanghao on 2017/4/28.
 */

@SuppressLint("SetJavaScriptEnabled")
public class DetailActivity extends BaseActionBarActivity{

    private static final String TAG = "DetailActivity";
    @BindView(R.id.gankcontent_pg)
    ProgressBar progressBar;
    @BindView(R.id.gank_content_wb)
    MyScrollWebView webView;
    private String URL;
    private String TITLE;
    private View rootView;
    WebSettings webSettings;
    private boolean isFullScreen=false;
    @Override
    protected int setContentLayout() {
        return R.layout.detail_layout;
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setTitle(TITLE);
        initView();
    }


    private void getIntentData() {
        Intent intent = getIntent();
        URL = intent.getStringExtra("url");
        TITLE = intent.getStringExtra("title");
    }

    private void initView() {
        rootView=getWindow().getDecorView();
        webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getApplicationContext().getDir("cache", 0).getPath());
        if (!NetWorkUtil.isNetWorkAvailable(this))
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        else
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSupportZoom(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null)
                    view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(chromeClient);
        webView.loadUrl(URL);
        webView.setScrollListener(new WebViewScrollListener() {
            @Override
            public void hideToolbar() {
                new Handler().postDelayed(()->{
                    hideSystemUI();
                },500);


            }

            @Override
            public void showToolbar() {
                new Handler().postDelayed(()->{
                    showSystemUI();
                },500);
            }
        });
    }

    private void hideSystemUI() {
        rootView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        rootView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }



    private WebChromeClient chromeClient=new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar != null) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                webView.reload();
                break;
            case R.id.menu_copy:
                copyUrlToClipBard();
                break;
            case R.id.menu_share:
                //// TODO: 2017/4/30 分享
                shareUrl();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareUrl() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,URL);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"分享到..."));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    private void copyUrlToClipBard() {
        ClipboardManager clipboardManager= (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("URL",URL);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this,"链接已复制到粘贴板",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);

    }
}
