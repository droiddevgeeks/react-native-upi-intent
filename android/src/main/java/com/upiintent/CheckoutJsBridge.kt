package com.upiintent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import android.webkit.JavascriptInterface
import com.facebook.react.bridge.ReactContext
import org.json.JSONArray
import org.json.JSONObject


internal class CheckoutJsBridge(private val context: Context) {

  @JavascriptInterface
  fun getAppList(name: String?): String {
    val intent = getIntent(name)
    val packageNames = JSONArray()
    try {
      val pm: PackageManager = context.packageManager
      val resInfo: List<ResolveInfo> = pm.queryIntentActivities(intent, 0)
      for (info in resInfo) {
        val appInfo = JSONObject()
        appInfo.put(
          "appName",
          pm.getApplicationLabel(info.activityInfo.applicationInfo) as String
        )
        appInfo.put("appPackage", info.activityInfo.packageName)
        packageNames.put(appInfo)
      }
    } catch (ex: Exception) {
      ex.printStackTrace()
    }
    return packageNames.toString()
  }

  @JavascriptInterface
  fun openApp(upiClientPackage: String?, upiURL: String?): Boolean {
    val intent = getIntent(upiURL)
    try {
      var foundPackageFlag = false
      val resInfo: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, 0)
      var upiClientResolveInfo: ResolveInfo? = null
      for (info in resInfo) {
        if (info.activityInfo.packageName.equals(upiClientPackage, true)) {
          foundPackageFlag = true
          upiClientResolveInfo = info
          break
        }
      }

      if (foundPackageFlag) {
        intent.setClassName(
          upiClientResolveInfo!!.activityInfo.packageName,
          upiClientResolveInfo.activityInfo.name
        )

        when (context) {
          is ReactContext -> context.currentActivity?.startActivityForResult(intent, 1000)
        }
      }
    } catch (exception: Exception) {
      Log.e(JsBridgeModuleImpl.TAG, "${exception.message}")
    }
    return true
  }

  private fun getIntent(upiURL: String?): Intent {
    return Intent().apply {
      setAction(Intent.ACTION_VIEW)
      setData(Uri.parse(upiURL))
    }
  }

}
