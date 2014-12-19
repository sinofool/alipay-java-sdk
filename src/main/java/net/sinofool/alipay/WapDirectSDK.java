package net.sinofool.alipay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.sinofool.alipay.base.AbstractAlipay;
import net.sinofool.alipay.base.StringPair;

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
            final String callBackUrl) {
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
        a.setReqString("call_back_url", callBackUrl);
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
            String auth = http.get("wappaygw.alipay.com", 443, "https", "/service/rest.htm?" + join(p, true));
            Map<String, String> params = AlipayResponseData.parse(auth);
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
            String token = res.getResString("request_token");
            if (token == null) {
                LOG.trace("Cannot find token in response");
                return null;
            }
            p = sign(makeWapAuthAndExecute(token).getSortedParameters());
            return "https://wappaygw.alipay.com/service/rest.htm?" + join(p, true);
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

    private void decrypt(Map<String, String> params) {
        if (preferRSA && params.get("sec_id").equals(SIGN_WAP_RSA)) {
            params.put("res_data", decrypt(params.get("res_data")));
        }
    }

    public boolean verify(final AlipayResponseData response) {
        List<StringPair> parameterList = response.getSortedParameters("sign");
        if (response.getString("sec_id").equals(SIGN_WAP_RSA)) {
            return verifyRSA(response.getString("sign"), parameterList);
        } else if (response.getString("sec_id").equals(SIGN_WAP_MD5)) {
            return verifyMD5(response.getString("sign"), parameterList);
        } else {
            return false;
        }
    }
}
