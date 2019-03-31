package com.eric.bean;

public class Api {
    private Integer apiId;

    private String apiContent;

    private String apiMad5;

    public Api() {
    }

    public Api(String apiContent, String apiMad5) {
        this.apiContent = apiContent;
        this.apiMad5 = apiMad5;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getApiContent() {
        return apiContent;
    }

    public void setApiContent(String apiContent) {
        this.apiContent = apiContent == null ? null : apiContent.trim();
    }

    public String getApiMad5() {
        return apiMad5;
    }

    public void setApiMad5(String apiMad5) {
        this.apiMad5 = apiMad5 == null ? null : apiMad5.trim();
    }

    @Override
    public String toString() {
        return apiId + ":" + apiContent + ":" + apiMad5;
    }
}