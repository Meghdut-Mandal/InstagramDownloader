package com.meghdut.instagram.downloader.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.apps.inslibrary.LoginHelper
import com.meghdut.instagram.downloader.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.backButton.setOnClickListener { finish() }
        loginBinding.swipeRefreshLayout.setOnRefreshListener { loadPage(loginBinding) }
        loadPage(loginBinding)
    }


    private fun loadPage(loginBinding: ActivityLoginBinding) = loginBinding.apply {
        webView.settings.javaScriptEnabled = true
        webView.clearCache(true)
        webView.webViewClient = MyBrowser()
        webView.loadUrl("https://www.instagram.com/accounts/login/")
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView, i: Int) {
                swipeRefreshLayout.isRefreshing = i != 100
            }
        }
    }

    fun getCookie(str: String?, str2: String?): String? {
        val cookie = CookieManager.getInstance().getCookie(str)
        if (cookie != null && cookie.isNotEmpty()) {
            for (str3 in cookie.split(";").toTypedArray()) {
                if (str3.contains(str2!!)) {
                    return str3.split("=").toTypedArray()[1]
                }
            }
        }
        return null
    }

    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(webView: WebView, str: String): Boolean {
            webView.loadUrl(str)
            return true
        }

        override fun onPageFinished(webView: WebView, str: String) {
            super.onPageFinished(webView, str)
            val cookie = CookieManager.getInstance().getCookie(str)
            try {
                val cookie2 = getCookie(str, "sessionid")
                val cookie3 = getCookie(str, "csrftoken")
                val cookie4 = getCookie(str, "ds_user_id")
                Log.d("TAG", "cookies:$cookie")
                if (TextUtils.isEmpty(cookie4) || TextUtils.isEmpty(cookie2)) {
                    return
                }
                LoginHelper.addUserID(cookie4)
                LoginHelper.addCSRF(cookie3)
                LoginHelper.addSessionID(cookie2)
                LoginHelper.setIsLogin(true)
                LoginHelper.setCookies(cookie)
                webView.destroy()
                val intent = Intent()
                intent.putExtra("userid", cookie4)
                intent.putExtra("sessionid", cookie2)
                intent.putExtra("csrftoken", cookie3)
                this@LoginActivity.setResult(-1, intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}