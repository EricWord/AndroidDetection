package com.eric.bean;

import java.util.List;

public class Apk {
    private Integer apkId;

    private String packageName;

    private Integer apkAttribute;
   private List<Api> apiList;
   private List<Authority> authorityList;

    public Apk() {
    }

    public Apk(String packageName, Integer apkAttribute) {
        this.packageName = packageName;
        this.apkAttribute = apkAttribute;
    }

    public Integer getApkId() {
        return apkId;
    }

    public void setApkId(Integer apkId) {
        this.apkId = apkId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    public Integer getApkAttribute() {
        return apkAttribute;
    }

    public void setApkAttribute(Integer apkAttribute) {
        this.apkAttribute = apkAttribute;
    }


    public List<Api> getApiList() {
        return apiList;
    }

    public void setApiList(List<Api> apiList) {
        this.apiList = apiList;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    @Override
    public String toString() {
        return "Apk{" +
                "apkId=" + apkId +
                ", packageName='" + packageName + '\'' +
                ", apkAttribute=" + apkAttribute +
                ", apiList=" + apiList +
                ", authorityList=" + authorityList +
                '}';
    }
}