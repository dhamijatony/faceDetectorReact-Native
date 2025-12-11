package com.visioncameramlkitfaces

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin

class VisionCameraMlkitFacesPackage : TurboReactPackage() {
    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? =
        if (name == "VisionCameraMlkitFaces") {
            VisionCameraMlkitFacesModule(reactContext)
        } else null

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider =
        ReactModuleInfoProvider {
            mapOf(
                "VisionCameraMlkitFaces" to
                        ReactModuleInfo(
                            "VisionCameraMlkitFaces",
                            VisionCameraMlkitFacesModule::class.java.name,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true,  // hasConstants
                            false, // isCxxModule
                            true   // isTurboModule
                        )
            )
        }

    // THIS is important: register our frame processor plugin(s)
    override fun createFrameProcessorPlugins(): List<FrameProcessorPlugin> {
        return listOf(FaceDetectorPlugin())
    }
}
