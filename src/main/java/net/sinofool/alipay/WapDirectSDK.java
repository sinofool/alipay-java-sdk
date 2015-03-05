package net.sinofool.alipay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import net.sinofool.alipay.base.AbstractAlipay;
import net.sinofool.alipay.base.GroupStringPair;
import net.sinofool.alipay.base.StringPair;
import net.sinofool.alipay.dict.AlipayWapRequestCreateDict;

import org.xml.sax.SAXException;

public class WapDirectSDK extends AbstractAlipay {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WapDirectSDK.class);
    private static final String SIGN_WAP_RSA = "0001";
    private static final String SIGN_WAP_MD5 = "MD5";

    private final AlipayHttpClient http;

    public WapDirectSDK(final AlipayConfig config, AlipayHttpClient httpClient) {
        super(config);
        this.http = httpClient;
    }

    public AlipayRequestData makeSimpleWapTradeDirect(final String tradeId, final String subject, final double total,
            final String callBackUrl, final String notifyUrl, final String merchantUrl) {
        AlipayRequestData a = new AlipayRequestData();
        a.setString("service", "alipay.wap.trade.create.direct");
        a.setString("format", "xml");
        a.setString("v", "2.0");
        a.setString("partner", config.getPartnerId());
        a.setString("req_id", tradeId);
        a.setString("req_data", "direct_trade_create_req");
        a.setReqString("subject", subject);
        a.setReqString("out_trade_no", tradeId);
        a.setReqString("total_fee", String.valueOf(total));
        a.setReqString("seller_account_name", config.getSellerAccount());
        a.setReqString(AlipayWapRequestCreateDict.REQUIRED_REQDATA.CALL_BACK_URL, callBackUrl);
        a.setReqString(AlipayWapRequestCreateDict.OPTIONAL_REQDATA.NOTIFY_URL, notifyUrl);
        a.setReqString(AlipayWapRequestCreateDict.OPTIONAL_REQDATA.MERCHANT_URL, merchantUrl);
        return a;
    }

    private AlipayRequestData makeWapAuthAndExecute(String token) {
        AlipayRequestData a = new AlipayRequestData();
        a.setString("service", "alipay.wap.auth.authAndExecute");
        a.setString("format", "xml");
        a.setString("v", "2.0");
        a.setString("partner", config.getPartnerId());
        a.setString("req_data", "auth_and_execute_req");
        a.setReqString("request_token", token);
        return a;
    }

    private List<StringPair> sign(List<StringPair> p) {
        String sign = null;
        if (preferRSA) {
            p.add(new StringPair("sec_id", SIGN_WAP_RSA));
            Collections.sort(p);
            sign = signRSA(p);
        } else {
            p.add(new StringPair("sec_id", SIGN_WAP_MD5));
            Collections.sort(p);
            sign = signMD5(p);
        }
        p.add(new StringPair("sign", sign));
        return p;
    }

    public String create(final AlipayRequestData request) {
        List<StringPair> p = sign(request.getSortedParameters());
        try {
            String auth = http.get("wappaygw.alipay.com", 443, "https", "/service/rest.htm?" + join(p, true, false));
            GroupStringPair params = parseQueryString(auth);
            decrypt(params);
            AlipayResponseData res = AlipayResponseData.parse(params);
            boolean verified = false;
            if (preferRSA && res.getString("sec_id").equals(SIGN_WAP_RSA)) {
                verified = verifyRSA(res.getString("sign"), res.getSortedParameters("sign"));
            } else if (res.getString("sec_id").equals(SIGN_WAP_MD5)) {
                verified = verifyMD5(res.getString("sign"), res.getSortedParameters("sign"));
            }
            if (!verified) {
                return null;
            }
            String token = res.getExtraString("request_token");
            if (token == null) {
                LOG.trace("Cannot find token in response");
                return null;
            }
            p = sign(makeWapAuthAndExecute(token).getSortedParameters());
            return "https://wappaygw.alipay.com/service/rest.htm?" + join(p, true, false);
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

    public void decrypt(final GroupStringPair params) {
        if (!preferRSA) {
            return;
        }
        String sec = params.get("sec_id");
        if (sec == null || !SIGN_WAP_RSA.equals(sec)) {
            return;
        }
        if (params.get("res_data") != null) {
            String value = params.get("res_data");
            params.update("res_data", decrypt(value));
        } else if (params.get("notify_data") != null) {
            String value = params.get("notify_data");
            params.update("notify_data", decrypt(value));
        }
    }

    public boolean verifyCallback(final AlipayResponseData response) {
        List<StringPair> parameterList = response.getOrderedParameters("sign", "sign_type");
        // This parameter "sign_type" is not documented.
        String type = response.getString("sign_type");
        if (type.equals(SIGN_WAP_RSA)) {
            return verifyRSA(response.getString("sign"), parameterList);
        } else if (type.equals(SIGN_WAP_MD5)) {
            return verifyMD5(response.getString("sign"), parameterList);
        } else {
            return false;
        }
    }

    public boolean verifyNotify(final AlipayResponseData response) {
        List<StringPair> parameterList = response.getOrderedParameters("sign");
        String type = response.getString("sec_id");
        if (type.equals(SIGN_WAP_RSA)) {
            StringBuilder content = new StringBuilder();
            content.append("service=").append(response.getString("service"));
            content.append("&v=").append(response.getString("v"));
            content.append("&sec_id=").append(response.getString("sec_id"));
            content.append("&notify_data=").append(response.getString("notify_data"));
            return rsaVerify(content.toString(), response.getString("sign"));
            // return verifyRSA(response.getString("sign"), parameterList);
        } else if (type.equals(SIGN_WAP_MD5)) {
            return verifyMD5(response.getString("sign"), parameterList);
        } else {
            return false;
        }
    }
}
