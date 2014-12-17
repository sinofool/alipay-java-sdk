package net.sinofool.alipay;

import java.util.ArrayList;
import java.util.List;

import net.sinofool.alipay.base.AbstractAlipay;
import net.sinofool.alipay.base.OneLevelOnlyXML;
import net.sinofool.alipay.base.StringPair;

public class AlipayWapDirect extends AbstractAlipay {

    public class WAPURLs {
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
        String auth = createWapTradeDirect(tradeId, subject, total, u);
        String token = verifyAndExtract(auth);
        return createWapAuthAndExecute(token);
    }

    private String verifyAndExtract(String auth) {
        
        return null;
    }

    private String createWapTradeDirect(final String tradeId, final String subject, final double total, final WAPURLs u) {
        List<StringPair> p = new ArrayList<StringPair>();
        p.add(new StringPair("service", "alipay.wap.trade.create.direct"));
        p.add(new StringPair("format", "xml"));
        p.add(new StringPair("v", "2.0"));
        p.add(new StringPair("partner", config.getPartnerId()));
        p.add(new StringPair("req_id", tradeId));
        p.add(new StringPair("req_data", wapTradeData(tradeId, subject, total, u)));
        p.add(new StringPair("sec_id", "MD5"));
        String sign = signMD5(p);
        p.add(new StringPair("sign", sign));

        return join(p);
    }

    private String createWapAuthAndExecute(String token) {
        List<StringPair> p = new ArrayList<StringPair>();
        p.add(new StringPair("service", "alipay.wap.auth.authAndExecute"));
        p.add(new StringPair("format", "xml"));
        p.add(new StringPair("v", "2.0"));
        p.add(new StringPair("partner", config.getPartnerId()));
        p.add(new StringPair("req_data", wapAuthAndExecuteData(token)));
        p.add(new StringPair("sec_id", "MD5"));
        String sign = signMD5(p);
        p.add(new StringPair("sign", sign));

        return join(p);
    }
}
