package com.eric.bean;

public class ApiApkMap {
    private Integer apkId;

    private Integer apiId;

    public ApiApkMap() {
    }

    public ApiApkMap(Integer apkId, Integer apiId) {
        this.apkId = apkId;
        this.apiId = apiId;
    }

    public Integer getApkId() {
        return apkId;
    }

    public void setApkId(Integer apkId) {
        this.apkId = apkId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    @Override
    public String toString() {
        return apkId+":"+apiId;
    }
}