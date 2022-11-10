package com.onedream.meituanhook.accessibility

import android.accessibilityservice.AccessibilityService
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.onedream.meituanhook.ClickPointHelper
import com.onedream.meituanhook.image.CaptureScreenService
import com.onedream.meituanhook.image.ImageConfigureHelper
import com.onedream.meituanhook.image.ImageOpenCVHelper
import com.onedream.meituanhook.ocr.OCRAPI
import com.onedream.meituanhook.ocr.OCRResultModel
import com.onedream.meituanhook.ocr.toA
import com.onedream.meituanhook.shared_preferences.ScreenLocalStorage
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import java.io.File
import kotlin.concurrent.thread

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent?) {
        Log.e("ATU", "onAccessibilityEvent")
        accessibilityEvent?.let {
            Log.e("ATU", "onAccessibilityEvent= $it")
        }
    }

    override fun onInterrupt() {
        Log.e("ATU", "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.e("ATU", "onServiceConnected")
        thread {
            Thread.sleep(5000)
            //
            while (true) {
                //click(622f, 406f)
                //swipe(200f, 0f, 200f, 2000f, 600)
                manualCaptureScreenAndClick()
                autoCaptureScreenAndClick()
            }
        }
    }

    private fun manualCaptureScreenAndClick() {
        if (null != ClickPointHelper.testClickRect) {
            Log.e("ATU", "不为空")
            click(
                ClickPointHelper.testClickRect!!.exactCenterX(),
                ClickPointHelper.testClickRect!!.exactCenterY(),
                10
            )
        }
        Thread.sleep(2000)
    }


    private fun autoCaptureScreenAndClick() {
        if (null != ClickPointHelper.testClickRect) {
            return
        }
        CaptureScreenService.start(this)
        Thread.sleep(2000)
        val buttonImage = ImageConfigureHelper.getButtonPicturePath()
        val currentScreenImage = ImageConfigureHelper.getCurrentScreenPicturePath()
        //
        val buttonImageFile = File(buttonImage)
        val currentScreenImageFile = File(currentScreenImage)
        if (buttonImageFile.exists() && currentScreenImageFile.exists()) {
            //小图
            val buttonBitmap = BitmapFactory.decodeStream(buttonImageFile.inputStream())
            val buttonMat = Mat(buttonBitmap.height, buttonBitmap.width, CvType.CV_32FC1)
            Utils.bitmapToMat(buttonBitmap, buttonMat)
            //
            val screenBitmap = BitmapFactory.decodeStream(currentScreenImageFile.inputStream())
            //
            val topScreenBitmap = Bitmap.createBitmap(
                screenBitmap,
                0,
                0,
                screenBitmap.width,
                screenBitmap.height / 2
            )
            val topResultModel = OCRAPI.runModel(topScreenBitmap)
            Log.e("ATU", "上图识别结果:${topResultModel.toA()}")
            if (topResultModel is OCRResultModel.Success && topResultModel.data.price >= ScreenLocalStorage.getPrice()) {
                getResultRect(topScreenBitmap, buttonMat, screenBitmapHeight = 0)
            } else {
                Log.e("ATU", "上图忽略不点击，因为:${topResultModel.toA()}")
                val bottomScreenBitmap = Bitmap.createBitmap(
                    screenBitmap,
                    0,
                    screenBitmap.height / 2,
                    screenBitmap.width,
                    screenBitmap.height / 2
                )
                val bottomResultModel = OCRAPI.runModel(bottomScreenBitmap)
                Log.e("ATU", "下图识别结果:${bottomResultModel.toA()}")
                if (bottomResultModel is OCRResultModel.Success && bottomResultModel.data.price >= ScreenLocalStorage.getPrice()) {
                    getResultRect(
                        bottomScreenBitmap,
                        buttonMat,
                        screenBitmapHeight = screenBitmap.height / 2
                    )
                } else {
                    Log.e("ATU", "下图忽略不点击，因为:${bottomResultModel.toA()}")
                }
            }
            Thread.sleep(2000)
            //刷新按钮
            val buttonRefreshImageFile = File(ImageConfigureHelper.getButtonRefreshPicturePath())
            if (buttonRefreshImageFile.exists() && currentScreenImageFile.exists()) {
                val buttonRefreshBitmap = BitmapFactory.decodeStream(buttonRefreshImageFile.inputStream())
                val buttonRefreshMat = Mat(buttonRefreshBitmap.height, buttonRefreshBitmap.width, CvType.CV_32FC1)
                Utils.bitmapToMat(buttonRefreshBitmap, buttonRefreshMat)
                getResultRect(screenBitmap,buttonRefreshMat, 0)
            }
        }
    }

    fun getResultRect(areaBitmap: Bitmap, buttonMat: Mat, screenBitmapHeight: Int) {
        val target = Mat(areaBitmap.height, areaBitmap.width, CvType.CV_32FC1)//todo 截取高度一半
        Utils.bitmapToMat(areaBitmap, target)
        //
        val resultPicturePath = ImageConfigureHelper.getResultPicturePath()
        val rect = ImageOpenCVHelper.singleMatching(
            buttonMat,
            target,
            0.8f,
            resultPicturePath
        )
        if (null != rect) {
            Log.e("ATU", "不为空 点击坐标为x=" + rect.exactCenterX() + "====y=" + rect.exactCenterY())
            click(rect.exactCenterX(), screenBitmapHeight + rect.exactCenterY(), 10)
        }
    }
}