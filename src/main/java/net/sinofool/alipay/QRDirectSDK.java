package net.sinofool.alipay;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sinofool.alipay.base.AbstractAlipay;
import net.sinofool.alipay.base.GroupStringPair;
import net.sinofool.alipay.base.StringPair;
import net.sinofool.alipay.thirdparty.org.json.JSONArray;
import net.sinofool.alipay.thirdparty.org.json.JSONObject;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class QRDirectSDK extends AbstractAlipay {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(QRDirectSDK.class);

    public static class Sku {
        public Sku(String id, String name, String price) {
            this.sku_id = id;
            this.sku_name = name;
            this.sku_price = price;
        }

        private final String sku_id;
        private final String sku_name;
        private final String sku_price;

        public JSONObject toJSON() {
            JSONObject sku = new JSONObject();
            sku.put("sku_id", sku_id);
            sku.put("sku_name", sku_name);
            sku.put("sku_price", sku_price);
            return sku;
        }
    }

    public static class Good {
        public Good(String id, String name, String price, String skuTitle, Sku... sku) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.sku_title = skuTitle;
            this.sku = sku;
        }

        private final String id;
        private final String name;
        private final String price;
        private final String sku_title;
        private final Sku[] sku;

        public JSONObject toJSON() {
            JSONObject goods = new JSONObject();
            goods.put("id", id);
            goods.put("name", name);
            goods.put("price", price);
            goods.put("sku_title", sku_title);
            JSONArray skus = new JSONArray();
            for (Sku s : sku) {
                skus.put(s.toJSON());
            }
            goods.put("sku", skus);
            return goods;
        }
    }

    public static class ExtField {
        public ExtField(String inputTitle, String inputRegex) {
            this.input_title = inputTitle;
            this.input_regex = inputRegex;
        }

        private final String input_title;
        private final String input_regex;

        public JSONObject toJSON() {
            JSONObject extPhone = new JSONObject();
            extPhone.put("input_title", input_title);
            extPhone.put("input_regex", input_regex);
            return extPhone;
        }
    }

    public static class ExtInfo {
        public ExtInfo(String singleLimit, ExtField... extFields) {
            this.ext_field = extFields;
            this.single_limit = singleLimit;
        }

        private final ExtField[] ext_field;
        private final String single_limit;

        public JSONObject toJSON() {
            JSONObject extInfo = new JSONObject();
            JSONArray extFields = new JSONArray();
            for (ExtField ext : ext_field) {
                extFields.put(ext.toJSON());
            }
            extInfo.put("ext_field", extFields);
            extInfo.put("single_limit", single_limit);
            return extInfo;
        }
    }

    public static class BizData {
        public BizData(String tradeType, String needAddress, Good goodsInfo, String returnURL, String notifyURL,
                ExtInfo extInfo) {
            this.trade_type = tradeType;
            this.need_address = needAddress;
            this.goods_info = goodsInfo;
            this.return_url = returnURL;
            this.notify_url = notifyURL;
            this.ext_info = extInfo;
        }

        private final String trade_type;
        private final String need_address;
        private final Good goods_info;
        private final String return_url;
        private final String notify_url;
        private final ExtInfo ext_info;

        public JSONObject toJSON() {
            JSONObject biz = new JSONObject();
            biz.put("trade_type", trade_type);
            biz.put("need_address", need_address);
            biz.put("goods_info", goods_info.toJSON());
            biz.put("return_url", return_url);
            biz.put("notify_url", notify_url);
            biz.put("ext_info", ext_info.toJSON());
            return biz;
        }
    }

    private final AlipayHttpClient http;

    public QRDirectSDK(AlipayConfig config, AlipayHttpClient http) {
        super(config);
        this.http = http;
    }

    public AlipayRequestData makeAddDirectPayQR(final BizData bizData) {
        AlipayRequestData request = new AlipayRequestData();
        request.setString("service", "alipay.mobile.qrcode.manage");
        request.setString("partner", config.getPartnerId());
        request.setString("_input_charset", "utf-8");
        DateTime beijing = new DateTime().toDateTime(org.joda.time.DateTimeZone.forID("Asia/Shanghai"));
        request.setString("timestamp", beijing.toString("yyyy-MM-dd HH:mm:ss"));
        request.setString("method", "add");
        request.setString("biz_type", "10");
        request.setString("biz_data", bizData.toJSON().toString());
        return request;
    }

    private String sign(AlipayRequestData request) {
        return signMD5(request.getSortedParameters());
    }

    public String create(AlipayRequestData request) {
        List<StringPair> signed = request.getSortedParameters();
        signed.add(new StringPair("sign", sign(request)));
        request.setString("sign_type", "MD5");
        String body = join(signed, true, false);
        String post = http.post("mapi.alipay.com", 443, "https", "/gateway.do?_input_charset=utf-8", body);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(post)));
            doc.getDocumentElement().normalize();

            boolean isSuccess = doc.getElementsByTagName("is_success").item(0).getTextContent().equals("T");
            if (!isSuccess) {
                return null;
            }

            String sign = doc.getElementsByTagName("sign").item(0).getTextContent();

            GroupStringPair ret = new GroupStringPair();
            NodeList childNodes = doc.getElementsByTagName("response").item(0).getFirstChild().getChildNodes();
            for (int i = 0; i < childNodes.getLength(); ++i) {
                String name = childNodes.item(i).getNodeName();
                String text = childNodes.item(i).getTextContent();
                ret.add(name, text);
            }

            String calc = signMD5(ret.getSorted());
            if (calc.equals(sign)) {
                return ret.get("qrcode");
            }
        } catch (IOException e) {
            LOG.warn("Error parsing xml response", e);
        } catch (ParserConfigurationException e) {
            LOG.warn("Error parsing xml response", e);
        } catch (SAXException e) {
            LOG.warn("Error parsing xml response", e);
        }
        return null;
    }

    public boolean verifyReturn(AlipayResponseData p) {
        List<StringPair> params = p.getSortedParameters("sign", "sign_type");
        String sign = signMD5(params);
        return sign.equals(p.getString("sign"));
    }

    public boolean verifyNotify(AlipayResponseData p) {
        List<StringPair> params = p.getSortedParameters("sign", "sign_type");
        String sign = signMD5(params);
        return sign.equals(p.getString("sign"));
    }

}
