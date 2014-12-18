package net.sinofool.alipay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AlipayPCReturnData {
    private static Date parseDate(String n) {
        if (n == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(n);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Double parseDouble(String n) {
        if (n == null) {
            return null;
        }
        return Double.valueOf(n);
    }

    public static AlipayPCReturnData parse(final Map<String, String> p) {
        AlipayPCReturnData d = new AlipayPCReturnData();
        d.setSuccess(p.get("is_success").equals("T"));
        d.setSignType(p.get("sign_type"));
        d.setSign(p.get("sign"));
        d.setOutTradeNo(p.get("out_trade_no"));
        d.setSubject(p.get("subject"));
        d.setPaymentType(p.get("payment_type"));
        d.setExterface(p.get("exterface"));
        d.setTradeNo(p.get("trade_no"));
        d.setTradeStatus(p.get("trade_status"));
        d.setNotifyId(p.get("notify_id"));
        d.setNotifyTime(parseDate(p.get("notify_time")));
        d.setNotifyType(p.get("notify_type"));
        d.setSellerEmail(p.get("seller_email"));
        d.setBuyerEmail(p.get("buyer_email"));
        d.setSellerId(p.get("seller_id"));
        d.setBuyerId(p.get("buyer_id"));
        d.setTotalFee(parseDouble(p.get("total_fee")));
        d.setBody(p.get("body"));
        d.setExtraCommonParam(p.get("extra_common_param"));
        d.setAgentUserId(p.get("agent_user_id"));
        return d;
    }

    // Required
    private boolean isSuccess;
    private String signType;
    private String sign;

    // Optional
    private String outTradeNo;
    private String subject;
    private String paymentType;
    private String exterface;
    private String tradeNo;
    private String tradeStatus;
    private String notifyId;
    private Date notifyTime;
    private String notifyType;
    private String sellerEmail;
    private String buyerEmail;
    private String sellerId;
    private String buyerId;
    private Double totalFee;
    private String body;
    private String extraCommonParam;
    private String agentUserId;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getExterface() {
        return exterface;
    }

    public void setExterface(String exterface) {
        this.exterface = exterface;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public Date getNotifyTime() {
        if (notifyTime == null) {
            return null;
        }
        return (Date) notifyTime.clone();
    }

    public void setNotifyTime(Date notifyTime) {
        if (notifyTime == null) {
            return;
        }
        this.notifyTime = (Date) notifyTime.clone();
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public String getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(String agentUserId) {
        this.agentUserId = agentUserId;
    }

}
