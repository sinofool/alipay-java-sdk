package net.sinofool.alipay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AlipayPCNotifyData {
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

    private static Integer parseInteger(String n) {
        if (n == null) {
            return null;
        }
        return Integer.valueOf(n);
    }

    private static Boolean parseBoolean(String n) {
        if (n == null) {
            return null;
        }
        return !"N".equalsIgnoreCase(n);
    }

    public static AlipayPCNotifyData parse(final Map<String, String> p) {
        AlipayPCNotifyData d = new AlipayPCNotifyData();
        d.setNotifyTime(parseDate(p.get("notify_time")));
        d.setNotifyType(p.get("notify_type"));
        d.setNotifyId(p.get("notify_id"));
        d.setSignType(p.get("sign_type"));
        d.setSign(p.get("sign"));
        d.setOutTradeNo(p.get("out_trade_no"));
        d.setSubject(p.get("subject"));
        d.setPaymentType(p.get("payment_type"));
        d.setTradeNo(p.get("trade_no"));
        d.setTradeStatus(p.get("trade_status"));
        d.setGmtCreate(parseDate(p.get("gmt_create")));
        d.setGmtPayment(parseDate(p.get("gmt_payment")));
        d.setGmtClose(parseDate(p.get("gmt_close")));
        d.setRefundStatus(p.get("refund_status"));
        d.setGmtRefund(parseDate(p.get("gmt_refund")));
        d.setSellerEmail(p.get("seller_email"));
        d.setBuyerEmail(p.get("buyer_email"));
        d.setSellerId(p.get("seller_id"));
        d.setBuyerId(p.get("buyer_id"));
        d.setPrice(parseDouble(p.get("price")));
        d.setTotalFee(parseDouble(p.get("total_fee")));
        d.setQuantity(parseInteger(p.get("quantity")));
        d.setBody(p.get("body"));
        d.setDiscount(parseDouble(p.get("discount")));
        d.setIsTotalFeeAdjust(parseBoolean(p.get("is_total_fee_adjust")));
        d.setUseCoupon(parseBoolean(p.get("use_coupon")));
        d.setExtraCommonParam(p.get("extra_common_param"));
        d.setOutChannelType(p.get("out_channel_type"));
        d.setOutChannelAmount(p.get("out_channel_amount"));
        d.setOutChannelInst(p.get("out_channel_inst"));
        d.setBusinessScene(p.get("business_scene"));
        return d;
    }

    // Required
    private Date notifyTime;
    private String notifyType;
    private String notifyId;
    private String signType;
    private String sign;

    // Optional
    private String outTradeNo;
    private String subject;
    private String paymentType;
    private String tradeNo;
    private String tradeStatus;
    private Date gmtCreate;
    private Date gmtPayment;
    private Date gmtClose;
    private String refundStatus;
    private Date gmtRefund;
    private String sellerEmail;
    private String buyerEmail;
    private String sellerId;
    private String buyerId;
    private Double price;
    private Double totalFee;
    private Integer quantity;
    private String body;
    private Double discount;
    private Boolean isTotalFeeAdjust;
    private Boolean useCoupon;
    private String extraCommonParam;
    private String outChannelType;
    private String outChannelAmount;
    private String outChannelInst;
    private String businessScene;

    public Date getNotifyTime() {
        return (Date) notifyTime.clone();
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = (Date) notifyTime.clone();
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
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

    public Date getGmtCreate() {
        if (gmtCreate == null) {
            return null;
        }
        return (Date) gmtCreate.clone();
    }

    public void setGmtCreate(Date gmtCreate) {
        if (gmtCreate == null) {
            return;
        }
        this.gmtCreate = (Date) gmtCreate.clone();
    }

    public Date getGmtPayment() {
        if (gmtPayment == null) {
            return null;
        }
        return (Date) gmtPayment.clone();
    }

    public void setGmtPayment(Date gmtPayment) {
        if (gmtPayment == null) {
            return;
        }
        this.gmtPayment = (Date) gmtPayment.clone();
    }

    public Date getGmtClose() {
        if (gmtClose == null) {
            return null;
        }
        return (Date) gmtClose.clone();
    }

    public void setGmtClose(Date gmtClose) {
        if (gmtClose == null) {
            return;
        }
        this.gmtClose = (Date) gmtClose.clone();
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getGmtRefund() {
        if (gmtRefund == null) {
            return null;
        }
        return (Date) gmtRefund.clone();
    }

    public void setGmtRefund(Date gmtRefund) {
        if (gmtRefund == null) {
            return;
        }
        this.gmtRefund = (Date) gmtRefund.clone();
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Boolean getIsTotalFeeAdjust() {
        return isTotalFeeAdjust;
    }

    public void setIsTotalFeeAdjust(Boolean isTotalFeeAdjust) {
        this.isTotalFeeAdjust = isTotalFeeAdjust;
    }

    public Boolean getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(Boolean useCoupon) {
        this.useCoupon = useCoupon;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public String getOutChannelType() {
        return outChannelType;
    }

    public void setOutChannelType(String outChannelType) {
        this.outChannelType = outChannelType;
    }

    public String getOutChannelAmount() {
        return outChannelAmount;
    }

    public void setOutChannelAmount(String outChannelAmount) {
        this.outChannelAmount = outChannelAmount;
    }

    public String getOutChannelInst() {
        return outChannelInst;
    }

    public void setOutChannelInst(String outChannelInst) {
        this.outChannelInst = outChannelInst;
    }

    public String getBusinessScene() {
        return businessScene;
    }

    public void setBusinessScene(String businessScene) {
        this.businessScene = businessScene;
    }

}
