package com.dcapi.more;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.app.ProgressDialog;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.dcapi.mymap.R;

public class MoreMainActivity extends Activity {

    private String url = "http://news.sina.cn/?wm=3239_0001";
    private WebView webView;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_main);
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

        });
        //启用支持JavaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载，使用dialog
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                //newProgress 1-100之间的整数
                if(newProgress==100)
                {
                    //网页加载完毕，关闭ProgressDialog
                    closeDialog();
                }
                else
                {
                    //网页正在加载,打开ProgressDialog
                    openDialog(newProgress);
                }
            }

            private void closeDialog() {
                // TODO Auto-generated method stub
                if(dialog!=null&&dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
            }

            private void openDialog(int newProgress) {
                // TODO Auto-generated method stub
                if(dialog==null)
                {
                    dialog=new ProgressDialog(MoreMainActivity.this);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();

                }
                else
                {
                    dialog.setProgress(newProgress);
                }


            }
        });



    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            //Toast.makeText(this, webView.getUrl(), Toast.LENGTH_SHORT).show();
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
//                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
