package com.upiintent

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = JsBridgeModuleImpl.NAME)
class JsBridgeModule(private val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {


  override fun getName() = JsBridgeModuleImpl.NAME

  private val moduleImpl: JsBridgeModuleImpl by lazy {
    JsBridgeModuleImpl(reactContext)
  }

  @ReactMethod
  fun addJsBridge(reactTag: Double) {
    moduleImpl.addJsBridge(reactTag)
  }
}
