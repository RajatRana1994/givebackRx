package com.app.givebackrx.appcode.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.app.givebackrx.utils.extension.isInternetAvailable
import kotlinx.android.synthetic.main.activity_web.*
import android.webkit.WebChromeClient
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.app.givebackrx.R


class WebActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        var mProgressDialog: ProgressDialog? = null


        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(com.app.givebackrx.R.layout.activity_web)
        webView.settings.javaScriptEnabled=true
        webView.setWebViewClient(MyWebViewClient())
        mProgressDialog = initProgressDialog(this)
        if (isInternetAvailable())
            if(intent.hasExtra("url")){
                tvTitle.text=intent.getStringExtra("name")
                webView.loadUrl(intent.getStringExtra("url").toString())
            }else{
                tvTitle.text="Terms of Service"
                webView.loadUrl("https://givebackrx-marketplace-prod.herokuapp.com/terms_and_condition")
            }
        ivBack.setOnClickListener { finish() }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    mProgressDialog.hide()
                }
            }
        }



    }

    fun initProgressDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    private class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url);
            return true;
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}