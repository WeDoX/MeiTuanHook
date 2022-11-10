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
           while (true){
                //click(622f, 406f)
                //swipe(200f, 0f, 200f, 2000f, 600)
                manualCaptureScreenAndClick()
                autoCaptureScreenAndClick()
            }
        }
    }

    private fun manualCaptureScreenAndClick(){
        if(null != ClickPointHelper.testClickRect){
            Log.e("ATU","不为空")
            click(ClickPointHelper.testClickRect!!.exactCenterX(), ClickPointHelper.testClickRect!!.exactCenterY(), 10)
        }
        Thread.sleep(2000)
    }


    private fun autoCaptureScreenAndClick(){
        if(null != ClickPointHelper.testClickRect){
            return
        }
        CaptureScreenService.start(this)
        Thread.sleep(2000)
        val buttonImage =  ImageConfigureHelper.getButtonPicturePath()
        val currentScreenImage = ImageConfigureHelper.getCurrentScreenPicturePath()
        //
        val buttonImageFile = File(buttonImage)
        val currentScreenImageFile = File(currentScreenImage)
        if (buttonImageFile.exists() && currentScreenImageFile.exists()) {
            //小图
            val buttonBitmap  = BitmapFactory.decodeStream(buttonImageFile.inputStream())
            val buttonMat = Mat(buttonBitmap.height, buttonBitmap.width, CvType.CV_32FC1)
            Utils.bitmapToMat(buttonBitmap, buttonMat)
            //
            val screenBitmap = BitmapFactory.decodeStream(currentScreenImageFile.inputStream())
            //
            val topScreenBitmap = Bitmap.createBitmap(screenBitmap, 0,0,screenBitmap.width,screenBitmap.height/2)
            OCRAPI.run_model(topScreenBitmap, error = {
                Log.e("ATU","上图==》OCR识别错误，原因为:${it}")
            },success = {
                Log.e("ATU","上图==》OCR识别成功，结果为：${it}")
                if(it.price < 10 || it.time > 60){
                    val bottomScreenBitmap = Bitmap.createBitmap(screenBitmap, 0,screenBitmap.height/2,screenBitmap.width,screenBitmap.height/2)
                    OCRAPI.run_model(bottomScreenBitmap, error = {
                        Log.e("ATU","下图==》OCR识别错误，原因为:${it}")
                    },success = {
                        Log.e("ATU","下图==》OCR识别成功，结果为：${it}")
                        getResultRect(bottomScreenBitmap, buttonMat, screenBitmapHeight = screenBitmap.height/2)
                    })
                }else{
                    getResultRect(topScreenBitmap, buttonMat, screenBitmapHeight = 0)
                }
            })
        }
        Thread.sleep(2000)
    }

    fun getResultRect(areaBitmap: Bitmap, buttonMat :Mat, screenBitmapHeight : Int){
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
        if(null != rect){
            Log.e("ATU","不为空 点击坐标为x="+ rect.exactCenterX()+"====y="+rect.exactCenterY())
            click(rect.exactCenterX(),screenBitmapHeight + rect.exactCenterY(), 10)
        }
    }
}