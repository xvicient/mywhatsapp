package com.xvicient.mywhatsapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView
import com.xvicient.mywhatsapp.databinding.ActivityFullscreenBinding
import android.webkit.WebViewClient
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding

    private var isFreeNowBoard: Boolean =
        when (Build.MODEL) {
            "21051182G" -> true
            else -> { false }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // WebView
        setContentView(R.layout.activity_fullscreen)
        val webView = findViewById<WebView>(R.id.webview)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setAppCacheEnabled(false)
        webSettings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
        webView.clearCache(true)

        enableDesktopMode(webView)

        // Add a WebViewClient and hide board banner
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.evaluateJavascript("$('#dak-banner').hide();") { } // Hide banner
                super.onPageFinished(view, url)
            }
        }

        val boardId = if (isFreeNowBoard) Constants.FREENOW_BOARD_ID else Constants.XAVILAIA_BOARD_ID
        webView.loadUrl("https://web.whatsapp.com")
        if (!isFreeNowBoard) webView.setInitialScale(100)
    }

    object Constants {
        const val FREENOW_BOARD_ID = "2011fae0603699401623839badb091d3"
        const val XAVILAIA_BOARD_ID = "9eb76cbf9dac8836bee512684b4a48c2"
    }

    private fun enableDesktopMode(webView: WebView) {
        webView.settings.domStorageEnabled = true

        var userAgent = webView.settings.userAgentString

        try {
            val androidString = webView.settings.userAgentString.substring(
                userAgent.indexOf("("),
                userAgent.indexOf(")") + 1
            )
            userAgent = webView.settings.userAgentString.replace(androidString, "X11; Linux x86_64")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        webView.settings.userAgentString = userAgent
        webView.reload()
    }
}
