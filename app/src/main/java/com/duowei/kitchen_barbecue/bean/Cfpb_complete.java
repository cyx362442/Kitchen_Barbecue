package com.duowei.kitchen_barbecue.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017-09-14.
 */

public class Cfpb_complete extends DataSupport{
    private String xmbh;
    private String xmmc;
    private String zh;
    private float sl;
    private String pz;
    private String xdsj;
    private String wcsj;
    private String zzry;
    private String yhmc;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getYhmc() {
        return yhmc;
    }

    public void setYhmc(String yhmc) {
        this.yhmc = yhmc;
    }

    public String getXmbh() {
        return xmbh;
    }

    public void setXmbh(String xmbh) {
        this.xmbh = xmbh;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public float getSl() {
        return sl;
    }

    public void setSl(float sl) {
        this.sl = sl;
    }

    public String getPz() {
        return pz;
    }

    public void setPz(String pz) {
        this.pz = pz;
    }

    public String getXdsj() {
        return xdsj;
    }

    public void setXdsj(String xdsj) {
        this.xdsj = xdsj;
    }

    public String getWcsj() {
        return wcsj;
    }

    public void setWcsj(String wcsj) {
        this.wcsj = wcsj;
    }

    public String getZzry() {
        return zzry;
    }

    public void setZzry(String zzry) {
        this.zzry = zzry;
    }
}
