package net.sinofool.alipay.demo;

import net.sinofool.alipay.AlipayRequestData;
import net.sinofool.alipay.AlipayResponseData;
import net.sinofool.alipay.PCDirectSDK;
import net.sinofool.alipay.base.StringPair;
import net.sinofool.alipay.dict.AlipayPCNotifyDict;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PCHandler {
    private static final Random RAND = new Random();

    void processBegin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 这个名称将显示在支付宝的后台,商户和用户都可以看到.
        String dt = new SimpleDateFormat("yyyyMMddHHMM").format(new Date());
        String tradeId = "TEST_TRADE_DEMO_" + dt + String.format("_%02d", RAND.nextInt(99));

        // 支付时显示的名称
        String subject = "AlipayJavaSDK测试";

        // 金额,0.01是人民币1分.
        double total = 0.01;

        // TODO Replace with your server URL (Carefully about HTTPS)
        // Alipay server will call these two URL.
        // This is unencrypted because Alipay server does not support a strong encryption type.
        // It is very likely they are still running Java 6 which does not support 2048 DH size.
        // Alternative option is you can setup another domain name using a lower encryption configuration.
        String notifyURL = "http://oneskill.com/alipay-java-sdk-demo/pc/notify";
        String errorNotifyURL = "http://oneskill.com/alipay-java-sdk-demo/pc/error-notify";

        // TODO Replace with your server URL (Carefully about HTTPS)
        // This is alipay will redirect user to, can be HTTPS.
        // But this result is not trustful, only trust the data of server-to-server communication to notifyURL.
        // Normally, this call is late than notify, so I strongly recommended check the transaction status.
        String returnURL = "https://oneskill.com/alipay-java-sdk-demo/pc/return";

        PCDirectSDK pcSdk = DemoHelper.getInstance().getPCSDK();
        AlipayRequestData alipayReq = pcSdk.makeSimpleDirectPayRequest(tradeId, subject, total, notifyURL, returnURL, errorNotifyURL);
        List<StringPair> alipayFields = pcSdk.create(alipayReq);
        String alipayActionUrl = pcSdk.getActionURL();

        // TODO Replace this with your own rendering engine.
        StringBuilder html = new StringBuilder();
        html.append("<html><body>" +
                "<form action=\"" + alipayActionUrl + "\" method=\"POST\">\n" +
                "\t\t<table>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<th>KEY</th>\n" +
                "\t\t\t\t<th>VALUE</th>\n" +
                "\t\t\t</tr>\n");

        for (StringPair field : alipayFields) {
            html.append("\t\t\t<tr>\n" +
                    "\t\t\t\t<td>" + field.getFirst() + "</td>\n" +
                    "\t\t\t\t<td><input type=\"text\" name=\"" + field.getFirst() + "\"\n" +
                    "\t\t\t\t\tvalue=\"" + field.getSecond() + "\" /></td>\n" +
                    "\t\t\t</tr>\n");
        }
        html.append("\t\t</table>\n" +
                "\t\t<input type=\"submit\" value=\"Go to Alipay.com\" />\n" +
                "\t</form>");

        html.append("<hr />" + DemoHelper.getInstance().getLog());
        html.append("</body></html>");


        // This is important if you have non-ascii characters in subject.
        resp.addHeader("Content-Type", "text/html; charset=utf-8");
        resp.getWriter().write(html.toString());
        DemoHelper.getInstance().appendLog(new Date() + " Created trade: " + tradeId);
    }

    void processNotify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParserConfigurationException, SAXException {
        PCDirectSDK pcSdk = DemoHelper.getInstance().getPCSDK();
        AlipayResponseData p = AlipayResponseData.parse(pcSdk.parsePostBody(req.getInputStream()));
        if (pcSdk.verify(p)) {
            String tradeId = p.getString(AlipayPCNotifyDict.OPTIONAL.OUT_TRADE_NO);
            DemoHelper.getInstance().appendLog(new Date() + " Correct " + tradeId + " trade notify received");
            resp.getWriter().write("success");
        } else {
            DemoHelper.getInstance().appendLog(new Date() + " Incorrect trade notify received");
        }
    }

    void processReturn(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParserConfigurationException, SAXException {
        PCDirectSDK pcSdk = DemoHelper.getInstance().getPCSDK();
        AlipayResponseData p = AlipayResponseData.parse(pcSdk.parseQueryString(req.getQueryString()));
        if (pcSdk.verify(p)) {
            String tradeId = p.getString(AlipayPCNotifyDict.OPTIONAL.OUT_TRADE_NO);
            DemoHelper.getInstance().appendLog(new Date() + " Correct " + tradeId + " trade return received");
        } else {
            DemoHelper.getInstance().appendLog(new Date() + " Incorrect trade return received");
        }
        resp.getWriter().write("<html><body>" + DemoHelper.getInstance().getLog() + "</body></html>");
    }
}
