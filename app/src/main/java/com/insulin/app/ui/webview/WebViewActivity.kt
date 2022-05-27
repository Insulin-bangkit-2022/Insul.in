package com.insulin.app.ui.webview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import com.insulin.app.R
import com.insulin.app.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.webview.let {
            it.loadUrl(intent.getStringExtra(EXTRA_WEBVIEW) ?: "https://insul-in.web.app")
            it.settings.javaScriptEnabled = true
            
            /* set title webview after finsihed load */
            it.webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    binding.progressBar.isVisible = true
                    view.loadUrl(url.toString())
                    return false
                }
                override fun onPageFinished(view: WebView, url: String) {
                    binding.progressBar.isVisible = false
                    binding.toolbar.title = view.title
                }
            }
            
            /* set progressbar loading */
            it.webChromeClient = object : WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.progressBar.progress = newProgress
                }
            }
        }


    }

    /* handling back action -> if not root of URL, webview can back to previous URL */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action === KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.webview.canGoBack()) {
                        binding.webview.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_webview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh -> {
                binding.webview.reload()
            }
            R.id.prev_page -> {
                if(binding.webview.canGoBack()){
                    binding.webview.goBack()
                }else{
                    Toast.makeText(this,"Tidak ada halaman sebelumnya",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.next_page -> {
                if(binding.webview.canGoForward()){
                    binding.webview.goForward()
                }else{
                    Toast.makeText(this,"Tidak ada halaman selanjutnya",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    companion object {
        const val EXTRA_WEBVIEW = "EXTRA_WEBVIEW"
    }
}