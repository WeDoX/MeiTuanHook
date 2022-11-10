package com.onedream.meituanhook.ocr

import android.content.Context
import android.graphics.Bitmap
import com.onedream.meituanhook.R
import com.onedream.ocr_aar.OCRResultHelper
import com.onedream.ocr_aar.Predictor
import com.onedream.ocr_aar.QCRMeiTuanItemModel

object OCRAPI {

    var predictor = Predictor()

    fun onLoadModel(context: Context): Boolean {
        if (predictor.isLoaded()) {
            predictor.releaseModel()
        }
        return predictor.init(
            context,
            context.getString(R.string.MODEL_PATH_DEFAULT),
            context.getString(R.string.LABEL_PATH_DEFAULT),
            0,
            context.getString(R.string.CPU_THREAD_NUM_DEFAULT).toInt(),
            context.getString(R.string.CPU_POWER_MODE_DEFAULT),
            context.getString(R.string.DET_LONG_SIZE_DEFAULT).toInt(),
            context.getString(R.string.SCORE_THRESHOLD_DEFAULT).toFloat()
        )
    }

    fun run_model(image: Bitmap, error: (String) -> Unit, success: (QCRMeiTuanItemModel) -> Unit) {
        if (!predictor.isLoaded()) {
            error.invoke("STATUS: model is not loaded")
            return
        }
        predictor.setInputImage(image)
        //
        Thread {
            // Run model if model is loaded
            if (predictor.isLoaded() && predictor.runModel(1, 0, 1)) {
                success.invoke(OCRResultHelper.processResult(predictor.outputResult()))
            } else {
                error.invoke("Run model failed!")

            }
        }.start()
    }
}