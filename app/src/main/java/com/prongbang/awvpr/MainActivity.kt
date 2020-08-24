package com.prongbang.awvpr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.prongbang.awvpr.databinding.ActivityMainBinding
import com.prongbang.awvpr.permissions.DexterPermissionsUtility

class MainActivity : AppCompatActivity() {

	private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
	private val webChromeClientUtility by lazy { WebChromeClientUtility(this) }
	private val dexterPermissionsUtility by lazy { DexterPermissionsUtility(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		initView()
	}

	override fun onResume() {
		super.onResume()
		initPermissions()
	}

	override fun onBackPressed() {
		if (binding.webView.canGoBack()) {
			binding.webView.goBack()
		} else {
			super.onBackPressed()
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (webChromeClientUtility.isNotMatchRequestCode(requestCode)) {
			super.onActivityResult(requestCode, resultCode, data)
		} else {
			webChromeClientUtility.onActivityResult(requestCode, resultCode, data)
		}
	}

	private fun initView() {
		binding.webView.apply {
			webChromeClient = webChromeClientUtility.getWebChromeClient {
				binding.progressBar.visibility = View.GONE
			}
			configureWebSettings(settings)
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private fun configureWebSettings(settings: WebSettings?) {
		settings?.apply {
			mediaPlaybackRequiresUserGesture = false
			javaScriptEnabled = true
			domStorageEnabled = true
			setAppCacheEnabled(true)

//			// File access
//			allowFileAccess = true
//			allowFileAccessFromFileURLs = true
//			allowContentAccess = true
//			allowUniversalAccessFromFileURLs = true
		}
	}

	private fun initPermissions() {
		dexterPermissionsUtility.checkCameraGranted(onNotGranted = {
			binding.permissionView.visibility = View.VISIBLE
		}, onGranted = {
			binding.permissionView.visibility = View.GONE
			binding.webView.loadUrl(BuildConfig.WEB_URL)
		})

		binding.apply {
			allowButton.setOnClickListener {
				dexterPermissionsUtility.requestCameraPermissions(onNotGranted = {
					permissionView.visibility = View.VISIBLE
				}, onGranted = {
					permissionView.visibility = View.GONE
					webView.loadUrl(BuildConfig.WEB_URL)
				})
			}
		}
	}

}