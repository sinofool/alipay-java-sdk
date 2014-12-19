package net.sinofool.alipay;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sinofool.alipay.base.StringPair;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AlipayResponseData {

    public static Map<String, String> parse(final String query) throws ParserConfigurationException, SAXException,
            IOException {
        Map<String, String> params = new HashMap<String, String>();
        String[] parameters = query.split("&");
        for (String param : parameters) {
            String[] kv = param.split("=");
            String key = null;
            String value = null;
            if (kv.length == 2) {
                key = URLDecoder.decode(kv[0], "utf-8");
                value = URLDecoder.decode(kv[1], "utf-8");
                params.put(key, value);
            }
        }
        return params;
    }

    public static <T> AlipayResponseData parse(Map<String, T> params) throws ParserConfigurationException,
            SAXException, IOException {
        AlipayResponseData a = new AlipayResponseData();
        for (Entry<String, T> param : params.entrySet()) {
            String value = null;
            if (param.getValue() instanceof String) {
                value = (String) param.getValue();
            } else if (param.getValue() instanceof String[]) {
                String[] v = (String[]) param.getValue();
                if (v.length != 1) {
                    continue;
                }
                value = v[0];
            }
            if (value == null) {
                // This is not possible;
                continue;
            }
            if (param.getKey().equals("res_data")) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(value)));
                Element root = doc.getDocumentElement();
                NodeList nodes = root.getChildNodes();
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Node node = nodes.item(i);
                    a.resData.put(node.getNodeName(), node.getTextContent());
                }
            }
            a.data.put(param.getKey(), value);
        }
        return a;
    }

    private Map<String, String> data = new HashMap<String, String>();
    private Map<String, String> resData = new HashMap<String, String>();

    public String getString(final String key) {
        return data.get(key);
    };

    public String getResString(final String key) {
        return resData.get(key);
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
        List<StringPair> ret = new LinkedList<StringPair>();
        for (Entry<String, String> param : data.entrySet()) {
            if (Arrays.binarySearch(skipKeys, param.getKey()) >= 0) {
                continue;
            }
            ret.add(new StringPair(param.getKey(), param.getValue()));
        }
        Collections.sort(ret);
        return ret;
    }
}
