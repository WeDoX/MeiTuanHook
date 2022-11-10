package com.onedream.meituan;

public class PageBean {
    private ItemBean top;
    private ItemBean bottom;

    public PageBean(ItemBean top, ItemBean bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    public ItemBean getTop() {
        return top;
    }

    public void setTop(ItemBean top) {
        this.top = top;
    }

    public ItemBean getBottom() {
        return bottom;
    }

    public void setBottom(ItemBean bottom) {
        this.bottom = bottom;
    }
}
