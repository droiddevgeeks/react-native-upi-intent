package com.upiintent

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import android.widget.FrameLayout
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.common.UIManagerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class JsBridgeModuleImpl(private val reactContext: ReactContext) :
  ActivityEventListener,
  LifecycleEventListener {

  init {
    reactContext.addActivityEventListener(this)
    reactContext.addLifecycleEventListener(this)
  }

  private val myPluginScope: CoroutineScope by lazy {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
  }

  private val uiManager by lazy {
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      UIManagerHelper.getUIManager(reactContext, UIManagerType.FABRIC)
    } else {
      reactContext.getNativeModule(UIManagerModule::class.java)
    }
  }

  private var webView: WebView? = null

  fun addJsBridge(reactTag: Double) {
    uiManager?.let { manager ->
      val id = reactTag.toInt()
      myPluginScope.launch {
        manager.resolveView(id)?.let { resolvedView ->
          if (resolvedView is FrameLayout) {
            val rnWebView = resolvedView.getChildAt(0)
            if (rnWebView != null && rnWebView is WebView) {
              webView = rnWebView
              checkAndAddJSBridge()
            }
          } else if (resolvedView is WebView) {
            webView = resolvedView
            checkAndAddJSBridge()
          }
        } ?: run {
          Log.d(TAG, "Unable to find view")
        }
      }
    } ?: run {
      Log.d(TAG, "UIManager is null. Can't add bridge")
    }
  }

  override fun onActivityResult(p0: Activity?, p1: Int, p2: Int, p3: Intent?) {
    if (p1 == 1000 && p2 == Activity.RESULT_OK && webView != null) {
      webView?.evaluateJavascript(VERIFY_BRIDGE) {}
    }
  }

  override fun onNewIntent(p0: Intent?) {
    //no impl
  }

  override fun onHostResume() {
    //no impl
  }

  override fun onHostPause() {
    //no impl
  }

  override fun onHostDestroy() {
    reactContext.removeActivityEventListener(this)
    reactContext.removeLifecycleEventListener(this)
    webView = null
  }

  private fun checkAndAddJSBridge() {
    webView?.let {
      webView?.evaluateJavascript("typeof window.$CHECKOUT_BRIDGE_NAME === 'undefined'") { result ->
        if (result == "true") {
          webView?.addJavascriptInterface(CheckoutJsBridge(reactContext), CHECKOUT_BRIDGE_NAME)
        }
      }
    }
  }

  companion object {
    const val NAME = "JsBridge"
    const val CHECKOUT_BRIDGE_NAME = "Android"
    const val TAG = "JsBridgeModule"
    const val VERIFY_BRIDGE = "window.showVerifyUI()"
  }
}
