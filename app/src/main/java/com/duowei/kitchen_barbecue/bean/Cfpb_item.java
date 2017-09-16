package com.duowei.kitchen_barbecue.bean;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Cfpb_item {
    public String xmbh1;
    public String czmc1;
    public float sl1;
    public int fzs;
    public String xh;
    public String pz;
    public String xszt;
    public String by10;
    public String cssj;
    public String wc;
    public String dw;
    public boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getWc() {
        return wc;
    }

    public void setWc(String wc) {
        this.wc = wc;
    }

    public String getBy10() {
        return by10;
    }

    public void setBy10(String by10) {
        this.by10 = by10;
    }

    public String getXh() {
        return xh;
    }

    public Cfpb_item(String xmbh1, String czmc1, float sl1, int fzs, String xh,
                     String pz, String xszt, String by10, String cssj,String dw,
                     boolean isSelect) {
        this.xmbh1 = xmbh1;
        this.czmc1 = czmc1;
        this.sl1 = sl1;
        this.fzs = fzs;
        this.xh = xh;
        this.pz = pz;
        this.xszt = xszt;
        this.by10 = by10;
        this.cssj=cssj;
        this.dw=dw;
        this.isSelect = isSelect;
    }
}
