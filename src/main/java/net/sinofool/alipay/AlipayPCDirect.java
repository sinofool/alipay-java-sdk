package net.sinofool.alipay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sinofool.alipay.base.AbstractAlipay;
import net.sinofool.alipay.base.StringPair;

public class AlipayPCDirect extends AbstractAlipay {

    public static class PCURLs {
        public String notifyURL;
        public String returnURL;
        public String errorNotifyURL;
    }

    public PCURLs createPCURLs(final String notifyURL, final String returnURL, final String errorNotifyURL) {
        PCURLs u = new PCURLs();
        u.notifyURL = notifyURL;
        u.returnURL = returnURL;
        u.errorNotifyURL = errorNotifyURL;
        return u;
    }

    public AlipayPCDirect(final AlipayConfig config) {
        super(config);
    }

    public String getPCActionURL() {
        return "https://mapi.alipay.com/gateway.do?_input_charset=utf-8";
    }

    public List<StringPair> createDirectPay(final String tradeId, final String subject, final double total, PCURLs u) {
        List<StringPair> p = new ArrayList<StringPair>();
        // Required, sign and sign_type at last
        p.add(new StringPair("service", "create_direct_pay_by_user"));
        p.add(new StringPair("partner", config.getPartnerId()));
        p.add(new StringPair("_input_charset", "utf-8"));

        // Optional
        p.add(new StringPair("notify_url", u.notifyURL));
        p.add(new StringPair("return_url", u.returnURL));
        p.add(new StringPair("error_notify_url", u.errorNotifyURL));

        // Required
        p.add(new StringPair("out_trade_no", tradeId));
        p.add(new StringPair("subject", subject));
        p.add(new StringPair("payment_type", "1"));
        p.add(new StringPair("total_fee", String.valueOf(total)));
        p.add(new StringPair("seller_id", config.getPartnerId()));
        String sign;
        if (preferRSA) {
            sign = signRSA(p);
            p.add(new StringPair("sign_type", "RSA"));
        } else {
            sign = signMD5(p);
            p.add(new StringPair("sign_type", "MD5"));
        }
        p.add(new StringPair("sign", sign));
        return p;
    }

    public List<StringPair> createNotifyVerify(String notifyId) {
        List<StringPair> p = new ArrayList<StringPair>();

        p.add(new StringPair("service", "notify_verify"));
        p.add(new StringPair("partner", config.getPartnerId()));
        p.add(new StringPair("_input_charset", "utf-8"));

        p.add(new StringPair("notify_id", notifyId));
        // sign(p);
        return p;
    }

    public boolean verify(final Map<String, String> parameterMap) {
        List<StringPair> parameterList = new ArrayList<StringPair>();
        for (Entry<String, String> entry : parameterMap.entrySet()) {
            if ("sign".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            if ("sign_type".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            parameterList.add(new StringPair(entry.getKey(), entry.getValue()));
        }
        if (parameterMap.get("sign_type").equals("RSA")) {
            return verifyRSA(parameterMap.get("sign"), parameterList);
        } else {
            String sign = signMD5(parameterList);
            return parameterMap.get("sign").equals(sign);
        }
    }
}
