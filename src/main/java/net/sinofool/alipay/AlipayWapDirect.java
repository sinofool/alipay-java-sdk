package net.sinofool.alipay;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sinofool.alipay.base.AbstractAlipay;
import net.sinofool.alipay.base.OneLevelOnlyXML;
import net.sinofool.alipay.base.StringPair;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AlipayWapDirect extends AbstractAlipay {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AlipayWapDirect.class);
    private static final String SIGN_WAP_RSA = "0001";
    private static final String SIGN_WAP_MD5 = "MD5";

    public static class WAPURLs {
        public String callbackURL;
    }

    public WAPURLs createWAPURLs(final String callbackURL) {
        WAPURLs u = new WAPURLs();
        u.callbackURL = callbackURL;
        return u;
    }

    private AlipayHttpClient http;

    public AlipayWapDirect(final AlipayConfig config, AlipayHttpClient httpClient) {
        super(config);
        this.http = httpClient;
    }

    private String wapTradeData(final String tradeId, final String subject, final double total, final WAPURLs u) {
        OneLevelOnlyXML xml = new OneLevelOnlyXML();
        xml.createRootElement("direct_trade_create_req");
        xml.createChild("subject", subject);
        xml.createChild("out_trade_no", tradeId);
        xml.createChild("total_fee", String.valueOf(total));
        xml.createChild("seller_account_name", config.getSellerAccount());
        xml.createChild("call_back_url", u.callbackURL);
        return xml.toXMLString();
    }

    private String wapAuthAndExecuteData(String token) {
        OneLevelOnlyXML xml = new OneLevelOnlyXML();
        xml.createRootElement("auth_and_execute_req");
        xml.createChild("request_token", token);
        return xml.toXMLString();
    }

    public String create(final String tradeId, final String subject, final double total, final WAPURLs u) {
        try {
            String path = "/service/rest.htm?" + createWapTradeDirect(tradeId, subject, total, u);
            System.out.println(path);
            String auth = http.get("wappaygw.alipay.com", 443, "https", path);
            System.out.println(auth);
            String token = verifyAndExtract(auth);
            if (token == null) {
                LOG.trace("Cannot find token in response");
                return null;
            }
            return "https://wappaygw.alipay.com/service/rest.htm?" + createWapAuthAndExecute(token);
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Failed to create wap direct trade", e);
            return null;
        } catch (SAXException e) {
            LOG.warn("Failed to create wap direct trade", e);
            return null;
        } catch (IOException e) {
            LOG.warn("Failed to create wap direct trade", e);
            return null;
        } catch (ParserConfigurationException e) {
            LOG.warn("Failed to create wap direct trade", e);
            return null;
        }
    }

    private String verifyAndExtract(String auth) throws SAXException, IOException, ParserConfigurationException {
        String[] parameters = auth.split("&");
        List<StringPair> par = new ArrayList<StringPair>();
        boolean success = true;
        String sign = null;
        String resData = null;
        String sec_id = null;
        for (String param : parameters) {
            String[] kv = param.split("=");
            String key = null;
            String value = null;
            if (kv.length == 2) {
                key = URLDecoder.decode(kv[0], "utf-8");
                value = URLDecoder.decode(kv[1], "utf-8");
                if (key.equals("res_error")) {
                    success = false;
                }
                if (key.equals("sign")) {
                    sign = value;
                    continue;
                }
                if (key.equals("res_data")) {
                    resData = value;
                }
                if (key.equals("sec_id")) {
                    sec_id = value;
                }
                par.add(new StringPair(key, value));
            } else if (kv.length == 1) {
                par.add(new StringPair(URLDecoder.decode(kv[0], "utf-8"), null));
            }
        }

        if (!success || sign == null) {
            LOG.debug("Failed to find sign for {}", auth);
            System.out.println(1);
            return null;
        }

        if (SIGN_WAP_MD5.equals(sec_id)) {
            if (!sign.equals(signMD5(par))) {
                LOG.debug("Failed to verify md5 for {}", auth);
                System.out.println(2);
                return null;
            }
        } else if (SIGN_WAP_RSA.equals(sec_id)) {
            for (StringPair p : par) {
                if (p.getFirst().equals("res_data")) {
                    resData = decrypt(p.getSecond());
                    if (resData == null) {
                        LOG.warn("Cannot decrypt res_data={}", p.getSecond());
                        return null;
                    }
                    p.setSecond(resData);
                }
            }
            if (!verifyRSA(sign, par)) {
                LOG.debug("Failed to verify rsa for {}", auth);
                return null;
            }
        } else {
            return null;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(resData)));
        NodeList token = doc.getDocumentElement().getElementsByTagName("request_token");
        if (token.getLength() == 1) {
            return token.item(0).getTextContent();
        }
        LOG.debug("Failed to extract request_token for {}", resData);
        return null;
    }

    private String createWapTradeDirect(final String tradeId, final String subject, final double total, final WAPURLs u)
            throws UnsupportedEncodingException {
        List<StringPair> p = new ArrayList<StringPair>();
        p.add(new StringPair("service", "alipay.wap.trade.create.direct"));
        p.add(new StringPair("format", "xml"));
        p.add(new StringPair("v", "2.0"));
        p.add(new StringPair("partner", config.getPartnerId()));
        p.add(new StringPair("req_id", tradeId));
        p.add(new StringPair("req_data", wapTradeData(tradeId, subject, total, u)));
        String sign = null;
        if (preferRSA) {
            p.add(new StringPair("sec_id", SIGN_WAP_RSA));
            sign = signRSA(p);
        } else {
            p.add(new StringPair("sec_id", SIGN_WAP_MD5));
            sign = signMD5(p);
        }
        p.add(new StringPair("sign", sign));

        return join(p, true);
    }

    private String createWapAuthAndExecute(String token) throws UnsupportedEncodingException {
        List<StringPair> p = new ArrayList<StringPair>();
        p.add(new StringPair("service", "alipay.wap.auth.authAndExecute"));
        p.add(new StringPair("format", "xml"));
        p.add(new StringPair("v", "2.0"));
        p.add(new StringPair("partner", config.getPartnerId()));
        p.add(new StringPair("req_data", wapAuthAndExecuteData(token)));
        String sign = null;
        if (preferRSA) {
            p.add(new StringPair("sec_id", SIGN_WAP_RSA));
            sign = signRSA(p);
        } else {
            p.add(new StringPair("sec_id", SIGN_WAP_MD5));
            sign = signMD5(p);
        }
        p.add(new StringPair("sign", sign));
        return join(p, true);
    }
}
