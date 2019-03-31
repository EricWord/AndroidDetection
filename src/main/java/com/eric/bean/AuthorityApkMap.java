package com.eric.bean;

public class AuthorityApkMap {
    private Integer apkId;

    private Integer authorityId;

    public AuthorityApkMap() {
    }

    public AuthorityApkMap(Integer apkId, Integer authorityId) {
        this.apkId = apkId;
        this.authorityId = authorityId;
    }

    public Integer getApkId() {
        return apkId;
    }

    public void setApkId(Integer apkId) {
        this.apkId = apkId;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public String toString() {
        return apkId+":"+authorityId;
    }
}