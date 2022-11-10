package com.onedream.ocr_aar;

import android.util.Log;

public class OCRResultHelper {

    public static String processResult(String result) {
        if (result == null) {
            return "";
        }
        Log.e("ATU","识别结果:"+result);
        result = result.replace("\r","");
        String processResult = "";
        int index = result.indexOf("分钟内");
        if (index > 0) {
            processResult += result.substring(index - 2, index);
            processResult += "分钟内";
            processResult += "=====》";
        }
        int priceIndex = result.indexOf("￥");
        if (priceIndex < 0) {
            priceIndex = result.indexOf("¥");
        }
        if (priceIndex > 0) {
            int dotIndex = result.indexOf(",", priceIndex);
            processResult += result.substring(priceIndex + 1, dotIndex);
            processResult += "元";
        }
        return processResult;
    }
}
