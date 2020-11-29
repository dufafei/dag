package org.mxgraph.flow.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLHandler {

    public static final String CR = System.getProperty("line.separator");

    public static String getXMLHeader() {
        return getXMLHeader("UTF-8");
    }
    public static String getXMLHeader(String encoding) { return "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" + CR; }

    /**
     * 使用Json String规则转义字符
     */
    public static String escapeXml10(String xml) {
        return StringEscapeUtils.escapeXml10(xml);
    }

    /**
     * 转义String使用XML实体中的字符
     */
    public static String escapeXml11(String xml) {
        return StringEscapeUtils.escapeXml11(xml);
    }

    /**
     * 反转义
     */
    public static String unescapeXml(String xml) {
        return StringEscapeUtils.unescapeXml(xml);
    }

    /**
     * 解析xml文件
     */
    public static Document loadXMLFile(InputStream inputStream, String systemID,
                                       boolean ignoreEntities, boolean namespaceAware) throws Exception {
        try {
            // 初始化一个XML解析工厂
            DocumentBuilderFactory factory = XMLParserFactoryProducer.createSecureDocBuilderFactory();
            // 指定由此代码生成的解析器将忽略注释。默认情况下，其值设置为 false
            factory.setIgnoringComments(true);
            // 指定由此代码生成的解析器将提供对XML名称空间的支持。默认情况下，其值设置为false。
            factory.setNamespaceAware(namespaceAware);
            // 创建一个DocumentBuilder实例
            DocumentBuilder db = factory.newDocumentBuilder();
            /*if (ignoreEntities) {
                db.setEntityResolver(new DTDIgnoringEntityResolver());
            }*/
            // 解析XML文档的输入流，得到一个Document
            Document doc;
            if (StringUtils.isEmpty(systemID)) {
                doc = db.parse(inputStream);
            } else {
                String systemIdWithEndingSlash = systemID.trim();
                if (!systemIdWithEndingSlash.endsWith("/") && !systemIdWithEndingSlash.endsWith("\\")) {
                    systemIdWithEndingSlash = systemIdWithEndingSlash.concat("/");
                }
                doc = db.parse(inputStream, systemIdWithEndingSlash);
            }
            return doc;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static Node getSubNode(Node n, String tag) {
        if (n == null) {
            return null;
        } else {
            NodeList children = n.getChildNodes();
            for(int i = 0; i < children.getLength(); ++i) {
                Node childNode = children.item(i);
                if (childNode.getNodeName().equalsIgnoreCase(tag)) {
                    return childNode;
                }
            }
            return null;
        }
    }

    public static List<Node> getNodes(Node n, String tag) {
        List<Node> nodes = new ArrayList<>();
        if (n == null) {
            return nodes;
        } else {
            NodeList children = n.getChildNodes();
            for(int i = 0; i < children.getLength(); ++i) {
                Node childNode = children.item(i);
                if (childNode.getNodeName().equalsIgnoreCase(tag)) {
                    nodes.add(childNode);
                }
            }
            return nodes;
        }
    }

    public static String getTagAttribute(Node node, String attribute) {
        if (node == null) {
            return null;
        } else {
            String retval = null;
            NamedNodeMap nnm = node.getAttributes();
            if (nnm != null) {
                Node attr = nnm.getNamedItem(attribute);
                if (attr != null) {
                    retval = attr.getNodeValue();
                }
            }
            return retval;
        }
    }

    public static String getTagValue(Node n, String tag) {
        if (n == null) {
            return null;
        } else {
            NodeList children = n.getChildNodes();
            for(int i = 0; i < children.getLength(); ++i) {
                Node childNode = children.item(i);
                if (childNode.getNodeName().equalsIgnoreCase(tag) && childNode.getFirstChild() != null) {
                    return childNode.getFirstChild().getNodeValue();
                }
            }
            return null;
        }
    }
}
