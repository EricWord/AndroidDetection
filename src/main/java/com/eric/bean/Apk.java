package com.eric.bean;

public class Apk {
    private Integer apkId;

    private String packageName;

    private Integer apkAttribute;
//    private Api api;
//    private Authority authority;

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

/*
    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
*/

    @Override
    public String toString() {
        return "Apk{" +
                "apkId=" + apkId +
                ", packageName='" + packageName + '\'' +
                ", apkAttribute=" + apkAttribute +
                '}';
    }
}