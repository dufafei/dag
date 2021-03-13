package com.github.dufafei.dag.codec.mxgraph;

import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.util.mxUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import javax.imageio.ImageIO;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.StringReader;

public class MxGraphUtil {

    private MxGraphUtil() {}

    public static void exportImage(String imageXML, String path, String name, int w, int h) throws Exception {
        if(!path.endsWith("/")){
            path = path + "/";
        }
        File png = new File(path + name + ".png");
        BufferedImage image = mxUtils.createBufferedImage(w, h, Color.WHITE);
        Graphics2D g2 = image.createGraphics();
        mxUtils.setAntiAlias(g2, true, true);
        mxGraphicsCanvas2D gc2 = new mxGraphicsCanvas2D(g2);
        gc2.setAutoAntiAlias(true);
        // 创建用于绘图到图形句柄的SAX处理程序
        mxSaxOutputHandler handler = new mxSaxOutputHandler(gc2);
        // 为处理程序创建SAX解析器
        XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        reader.setContentHandler(handler);
        // 将XML数据渲染成图像
        reader.parse(new InputSource(new StringReader(imageXML)));
        ImageIO.write(image, "png", png);
    }
}
