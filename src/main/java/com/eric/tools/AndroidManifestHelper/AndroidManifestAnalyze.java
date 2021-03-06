package com.eric.tools.AndroidManifestHelper;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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
//        String newPath = filePath.replace("AndroidManifest.xml", "AndroidManifestNew.xml");

//        CMDHelper.exeCmd("java -jar D:/cgs/software/jar/AXMLPrinter2.jar "+filePath,newPath);
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

    public static String findPackage(String filePath) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // 创建DocumentBuilder对象
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(filePath);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Node node = null;
        if (doc.getFirstChild() != null) {

            node = doc.getFirstChild();
        }
        NamedNodeMap attrs = null;
        if (null != node) {

            attrs = node.getAttributes();
        }
        if (null != attrs) {
            for (int i = 0; i < attrs.getLength(); i++) {
                if (attrs.item(i).getNodeName() == "package") {
                    return attrs.item(i).getNodeValue();
                }
            }
        }
        return null;
    }
}
