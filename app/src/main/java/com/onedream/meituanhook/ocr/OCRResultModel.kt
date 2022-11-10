package com.onedream.meituanhook.ocr

import com.onedream.ocr_aar.QCRMeiTuanItemModel

sealed class OCRResultModel<out T : Any> {
    class Success(val data: QCRMeiTuanItemModel) : OCRResultModel<QCRMeiTuanItemModel>()
    class Error(val data: String) : OCRResultModel<String>()
}

fun OCRResultModel<Any>.toA() :String{
    when(this){
        is OCRResultModel.Success->{
            return "OCR识别成功，结果为:${this.data}"
        }
        is OCRResultModel.Error->{
            return "OCR识别错误，原因为:${this.data}"
        }
    }
}