package com.onedream.meituan;

public class DataProducer {

    private static PageBean[] arr = {
            new PageBean(new ItemBean(9,"1.98"), new ItemBean(20,"20.8")),
            new PageBean(new ItemBean(50,"10.98"), new ItemBean(70,"20.8")),
            new PageBean(new ItemBean(19,"1.98"), new ItemBean(30,"30.8")),
            new PageBean(new ItemBean(60,"19.98"), new ItemBean(5,"20.8")),
            new PageBean(new ItemBean(10,"1.98"), new ItemBean(30,"20.8")),
            new PageBean(new ItemBean(51,"11.98"), new ItemBean(74,"24.8")),
            new PageBean(new ItemBean(20,"4.98"), new ItemBean(36,"39.8")),
            new PageBean(new ItemBean(60,"20.98"), new ItemBean(4,"25.8"))
    };

    public static PageBean getPage(){
        return arr[(int)(Math.random() * arr.length)];
    }
}
