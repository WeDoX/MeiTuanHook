package com.onedream.ocr_aar;

import android.util.Log;

public class OCRResultHelper {

    public static QCRMeiTuanItemModel processResult(String result) {
        QCRMeiTuanItemModel qcrMeiTuanItemModel = new QCRMeiTuanItemModel();
        if (result == null) {
            return qcrMeiTuanItemModel;
        }
        Log.e("ATU", "识别结果:" + result);
        try {
            result = result.replace("\r", "");
            String processResult = "";
            int index = result.indexOf("分钟内");
            if (index > 0) {
                processResult += result.substring(index - 2, index);
                processResult += "分钟内";
                processResult += "=====》";
                qcrMeiTuanItemModel.setTime(Integer.parseInt(result.substring(index - 2, index).replace(" ","")));
            }
            int priceIndex = result.indexOf("￥");
            if (priceIndex < 0) {
                priceIndex = result.indexOf("¥");
            }
            if (priceIndex > 0) {
                int dotIndex = result.indexOf(",", priceIndex);
                processResult += result.substring(priceIndex + 1, dotIndex);
                processResult += "元";
                qcrMeiTuanItemModel.setPrice(Double.parseDouble(result.substring(priceIndex + 1, dotIndex).replace(" ","")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qcrMeiTuanItemModel;
    }
}
