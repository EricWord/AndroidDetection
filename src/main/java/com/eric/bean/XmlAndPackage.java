package com.eric.bean;

/*
 *@description:androidManifest.xml和包名组合对象实体
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/3
 */
public class XmlAndPackage {
   private String packageName;
   private String xmlPath;

    public XmlAndPackage() {
    }

    public XmlAndPackage(String packageName, String xmlPath) {
        this.packageName = packageName;
        this.xmlPath = xmlPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }
}
