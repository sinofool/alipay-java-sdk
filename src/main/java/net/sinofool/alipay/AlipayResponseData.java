package net.sinofool.alipay;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sinofool.alipay.base.GroupStringPair;
import net.sinofool.alipay.base.StringPair;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AlipayResponseData {

    public static AlipayResponseData parse(GroupStringPair params) throws ParserConfigurationException, SAXException,
            IOException {
        AlipayResponseData ret = new AlipayResponseData();
        for (StringPair param : params.getOrdered()) {
            if (param.getFirst().equals("res_data") || param.getFirst().equals("notify_data")) {
                ret.extraKey = param.getFirst();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(param.getSecond())));
                Element root = doc.getDocumentElement();
                ret.extraRoot = root.getTagName();
                NodeList nodes = root.getChildNodes();
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Node node = nodes.item(i);
                    ret.extraData.add(node.getNodeName(), node.getTextContent());
                }
            }
            ret.data.add(param.getFirst(), param.getSecond());
        }
        return ret;
    }

    private GroupStringPair data = new GroupStringPair();
    private GroupStringPair extraData = new GroupStringPair();
    private String extraKey;
    private String extraRoot;

    public String getString(final String key) {
        return data.get(key);
    };

    public String getExtraKey() {
        return extraKey;
    }

    public String getExtraRoot() {
        return extraRoot;
    }

    public String getExtraString(final String key) {
        return extraData.get(key);
    }

    public Date getDate(final String key) throws ParseException {
        String v = getString(key);
        if (v == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v);
    }

    public Boolean getBoolean(final String key) {
        String v = getString(key);
        if (v == null) {
            return null;
        }
        if (v.equalsIgnoreCase("success")) {
            return true;
        }
        if (v.equalsIgnoreCase("N")) {
            return false;
        }
        if (v.equalsIgnoreCase("T")) {
            return true;
        }
        return null;
    }

    public List<StringPair> getSortedParameters(final String... skipKeys) {
        return data.getSorted(skipKeys);
    }

    public List<StringPair> getOrderedParameters(final String... skipKeys) {
        return data.getOrdered(skipKeys);
    }
}
