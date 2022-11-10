package com.onedream.meituan;

public class ItemBean {
    private int time;
    private String money;

    public ItemBean(int time, String money) {
        this.time = time;
        this.money = money;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
