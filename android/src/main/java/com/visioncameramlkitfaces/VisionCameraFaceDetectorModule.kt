package com.visioncameramlkitfaces

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.mrousavy.camera.frameprocessors.FrameProcessorPluginRegistry

class VisionCameraFaceDetectorModule(
  reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {


  override fun getName(): String = "VisionCameraFaceDetector"

 
}
