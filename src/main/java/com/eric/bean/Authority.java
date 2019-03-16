package com.eric.bean;

public class Authority {
    private Integer authorityId;

    private String authorityContent;

    private String authorityMd5;

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityContent() {
        return authorityContent;
    }

    public void setAuthorityContent(String authorityContent) {
        this.authorityContent = authorityContent == null ? null : authorityContent.trim();
    }

    public String getAuthorityMd5() {
        return authorityMd5;
    }

    public void setAuthorityMd5(String authorityMd5) {
        this.authorityMd5 = authorityMd5 == null ? null : authorityMd5.trim();
    }
}