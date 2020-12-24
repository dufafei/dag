package com.dataflow.frame.resolver.xml.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParserFactoryProducer {

    private static final Log logger = LogFactory.getLog(XMLParserFactoryProducer.class);

    public XMLParserFactoryProducer() {}

    public static DocumentBuilderFactory createSecureDocBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return docBuilderFactory;
    }
}
