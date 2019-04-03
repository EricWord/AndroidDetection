package com.eric.tools.AndroidManifestHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AndroidManifestAnalyze {
    /**
     * 解析入口
     *
     * @param filePath
     */
    public static List<String> xmlHandle(String filePath) {
        List<String> permissions = new ArrayList();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // 创建DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //加载xml文件
            Document document = null;
            try {
                //这个地方可能会有异常
                document = db.parse(filePath);
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + ":解析xml文件时出现异常");
            }
            NodeList permissionList = document.getElementsByTagName("uses-permission");
            //获取权限列表
            for (int i = 0; i < permissionList.getLength(); i++) {
                Node permission = permissionList.item(i);
                permissions.add((permission.getAttributes()).item(0).getNodeValue());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return permissions;
    }
}
