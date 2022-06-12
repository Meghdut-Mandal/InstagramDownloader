package com.meghdut.instagram.downloader.view;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.apps.inslibrary.LoginHelper;
import com.meghdut.instagram.downloader.R;


public class LoginActivity extends AppCompatActivity {
    LoginActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;
    private ImageView backButton;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        this.activity = this;
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        this.webView = (WebView) findViewById(R.id.webView);
        this.backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener( click -> {
            toBack(backButton);
        });
        loadPage();
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.meghdut.instagram.downloader.LoginActivity$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                LoginActivity.this.loadPage();
            }
        });
    }

    public void toBack(View view) {
        finish();
    }

    public void loadPage() {
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.clearCache(true);
        this.webView.setWebViewClient(new MyBrowser());
        CookieSyncManager.createInstance(this.activity);
        CookieManager.getInstance().removeAllCookie();
        this.webView.loadUrl("https://www.instagram.com/accounts/login/");
        this.webView.setWebChromeClient(new WebChromeClient() { // from class: com.meghdut.instagram.downloader.LoginActivity.1
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                LoginActivity.this.swipeRefreshLayout.setRefreshing(i != 100);
            }
        });
    }

    public String getCookie(String str, String str2) {
        String[] split;
        String cookie = CookieManager.getInstance().getCookie(str);
        if (cookie != null && !cookie.isEmpty()) {
            for (String str3 : cookie.split(";")) {
                if (str3.contains(str2)) {
                    return str3.split("=")[1];
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyBrowser extends WebViewClient {
        private MyBrowser() {
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            String cookie = CookieManager.getInstance().getCookie(str);
            try {
                String cookie2 = LoginActivity.this.getCookie(str, "sessionid");
                String cookie3 = LoginActivity.this.getCookie(str, "csrftoken");
                String cookie4 = LoginActivity.this.getCookie(str, "ds_user_id");
                Log.d("TAG", "cookies:" + cookie);
                if (TextUtils.isEmpty(cookie4) || TextUtils.isEmpty(cookie2)) {
                    return;
                }
                LoginHelper.addUserID(cookie4);
                LoginHelper.addCSRF(cookie3);
                LoginHelper.addSessionID(cookie2);
                LoginHelper.setIsLogin(true);
                LoginHelper.setCookies(cookie);
                webView.destroy();
                Intent intent = new Intent();
                intent.putExtra("userid", cookie4);
                intent.putExtra("sessionid", cookie2);
                intent.putExtra("csrftoken", cookie3);
                LoginActivity.this.setResult(-1, intent);
                LoginActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setStatusBarTextColor(boolean z) {
        View decorView = getWindow().getDecorView();
        if (z) {
            decorView.setSystemUiVisibility(9216);
        } else {
            decorView.setSystemUiVisibility(1280);
        }
    }

    public void setStatusBarColor(int i) {
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(getResources().getColor(i));
    }
}
