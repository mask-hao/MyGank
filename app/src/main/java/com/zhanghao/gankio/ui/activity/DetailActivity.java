package com.zhanghao.gankio.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.listener.NestedScrollListener;
import com.zhanghao.gankio.util.NetWorkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhanghao on 2017/4/28.
 */

public class DetailActivity extends BaseActivity {

    private static final String TAG = "DetailActivity";
    @BindView(R.id.gankcontent_pg)
    ProgressBar progressBar;
    @BindView(R.id.gank_content_wb)
    WebView webView;
    @BindView(R.id.gankcontent_sl)
    NestedScrollView scrollView;
    private String URL;
    private String TITLE;
    private View rootView;
    WebSettings webSettings;
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

        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setBuiltInZoomControls(true); // 支持手势缩放
        webSettings.setDisplayZoomControls(false); // 不显示缩放按钮

        webSettings.setDatabaseEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheEnabled(true);

        webSettings.setUseWideViewPort(true); // 将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true); // 自适应屏幕

        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        webView.loadUrl(URL);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(chromeClient);

        scrollView.setOnScrollChangeListener(new NestedScrollListener() {
            @Override
            public void hideToolBar() {
                if (mToolbar.isShow()){
                    mToolbar.hide();
//                    hideSystemUI();
                }
            }

            @Override
            public void showToolBar() {
                if (!mToolbar.isShow()){
                    mToolbar.show();
//                    showSystemUI();
                }

            }
        });
    }




    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        rootView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
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


}
