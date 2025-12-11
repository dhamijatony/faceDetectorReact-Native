package com.visioncameramlkitfaces

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfoProvider

class VisionCameraMlkitFacesPackage : TurboReactPackage() {
    override fun getModule(
        name: String,
        reactContext: ReactApplicationContext
    ): NativeModule? =
        if (name == "VisionCameraMlkitFaces") {
            VisionCameraMlkitFacesModule(reactContext)
        } else null

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider =
        ReactModuleInfoProvider {
            mapOf(
                "VisionCameraMlkitFaces" to
                        com.facebook.react.module.model.ReactModuleInfo(
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
}
