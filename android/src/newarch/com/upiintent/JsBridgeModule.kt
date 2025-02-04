package com.upiintent

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = JsBridgeModuleImpl.NAME)
class JsBridgeModule(private val reactContext: ReactApplicationContext) :
  NativeJsBridgeSpec(reactContext) {

  private val moduleImpl: JsBridgeModuleImpl by lazy {
    JsBridgeModuleImpl(reactContext)
  }

  override fun getName() = JsBridgeModuleImpl.NAME

  override fun addJsBridge(reactTag: Double) {
    moduleImpl.addJsBridge(reactTag)
  }

}
