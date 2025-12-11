package com.visioncameramlkitfaces

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.facebook.react.bridge.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class VisionCameraMlkitFacesModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    private val detector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .enableTracking()
            .build()

        FaceDetection.getClient(options)
    }

    override fun getName(): String = "VisionCameraMlkitFaces"

    @ReactMethod
    fun detectFaces(base64Image: String, promise: Promise) {
        try {
            val bytes = Base64.decode(base64Image, Base64.DEFAULT)
            val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val image = InputImage.fromBitmap(bitmap, 0)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    val arr = Arguments.createArray()
                    for (face in faces) {
                        arr.pushMap(faceToMap(face))
                    }
                    promise.resolve(arr)
                }
                .addOnFailureListener { e ->
                    promise.reject("ERR_DETECT_FACE", e)
                }

        } catch (e: Exception) {
            promise.reject("ERR_PROCESS", e)
        }
    }

    private fun faceToMap(face: Face): WritableMap {
        val m = Arguments.createMap()
        m.putInt("trackingId", face.trackingId ?: -1)

        val bounds = Arguments.createMap()
        bounds.putInt("x", face.boundingBox.left)
        bounds.putInt("y", face.boundingBox.top)
        bounds.putInt("width", face.boundingBox.width())
        bounds.putInt("height", face.boundingBox.height())
        m.putMap("bounds", bounds)

        m.putDouble("smileProbability", face.smilingProbability ?: -1.0)
        m.putDouble("leftEyeOpenProbability", face.leftEyeOpenProbability ?: -1.0)
        m.putDouble("rightEyeOpenProbability", face.rightEyeOpenProbability ?: -1.0)

        return m
    }
}
