package com.onedream.meituanhook.shared_preferences;

import com.onedream.meituanhook.shared_preferences.base.BaseLocalStorageManager;
import com.onedream.meituanhook.shared_preferences.base.LocalStorageConstant;

public class ScreenLocalStorage {

    public static int getScreenHeight(int defaultScreenHeight) {
        int height = BaseLocalStorageManager.getInt(LocalStorageConstant.KEY_SCREEN_HEIGHT, 0);
        if(height == 0){
            height = defaultScreenHeight;
        }
        return height;
    }

    public static void setScreenHeight(int screenHeight) {
        BaseLocalStorageManager.putInt(LocalStorageConstant.KEY_SCREEN_HEIGHT, screenHeight);
    }


    public static double getPrice(){
        double price = BaseLocalStorageManager.getDouble(LocalStorageConstant.KEY_PRICE, 0.0f);
        if(price <= 0){
            price = 0.0;
        }
        return price;
    }

    public static void setPrice(double price){
       BaseLocalStorageManager.putDouble(LocalStorageConstant.KEY_PRICE, price);
    }
}