package com.onedream.meituan;

public class DataProducer {

    public static PageBean getPage(){
        return new PageBean(new ItemBean(9,"1.98"), new ItemBean(20,"20.8"));
    }
}
