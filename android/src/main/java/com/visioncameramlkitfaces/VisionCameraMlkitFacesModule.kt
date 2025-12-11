package com.visioncameramlkitfaces

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.mrousavy.camera.frameprocessor.Frame
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class FaceDetectorPlugin : FrameProcessorPlugin("detectFaces") {

    private val detector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .enableTracking()
            .build()

        FaceDetection.getClient(options)
    }

    override fun callback(frame: Frame, params: Array<Any>): Any? {
        try {
            // Convert the frame into a byte array that ML Kit understands.
            // VisionCamera frame bytes may be in YUV (NV21) or RGBA depending on setup.
            // Here we handle NV21 (Image format NV21) — adjust if you use other formats.
            val width = frame.width
            val height = frame.height
            val rotation = frame.rotation // degrees
            val format = frame.format // FRAME_FORMAT_* constants (depends on vision-camera version)

            // Get bytes from frame
            val buffer: ByteBuffer = frame.byteBuffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            // Create ML Kit input image. If your frame pixel format differs, change IMAGE_FORMAT accordingly.
            // InputImage.IMAGE_FORMAT_NV21 works for the NV21 byte layout.
            val image = InputImage.fromByteArray(
                bytes,
                width,
                height,
                rotation,
                InputImage.IMAGE_FORMAT_NV21
            )

            // Detector.process() is async; block briefly using Tasks.await (synchronous).
            // This blocks the frame-processor thread for the duration of processing — typical but keep model/size small.
            val task = detector.process(image)
            val faces = Tasks.await(task, 5, TimeUnit.SECONDS) // timeout to avoid indefinite blocking

            // Convert faces -> WritableArray (JS-friendly)
            return facesToArray(faces)
        } catch (e: Exception) {
            Log.e("FaceDetectorPlugin", "face detection failed", e)
            return null
        }
    }

    private fun facesToArray(faces: List<Face>): WritableArray {
        val arr = Arguments.createArray()
        for (face in faces) {
            arr.pushMap(faceToMap(face))
        }
        return arr
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

        // convert Float? to Double for the React bridge
        m.putDouble("smileProbability", (face.smilingProbability ?: -1f).toDouble())
        m.putDouble("leftEyeOpenProbability", (face.leftEyeOpenProbability ?: -1f).toDouble())
        m.putDouble("rightEyeOpenProbability", (face.rightEyeOpenProbability ?: -1f).toDouble())

        // optional: add headEulerAngleY/Z (pose)
        m.putDouble("eulerY", face.headEulerAngleY.toDouble())
        m.putDouble("eulerZ", face.headEulerAngleZ.toDouble())

        // you can add contours, landmarks, etc. if needed

        return m
    }
}
