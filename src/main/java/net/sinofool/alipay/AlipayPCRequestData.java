package net.sinofool.alipay;

public class AlipayPCRequestData {
    // Required
    private String service;
    private String partner;
    private String inputCharset;
    private String signType;
    private String sign;

    // Optional
    private String notifyUrl;
    private String returnUrl;
    private String errorNotifyUrl;

    // Required
    private String outTradeNo;
    private String subject;
    private String paymentType;
    private double totalFee;
    private String sellerId;

    // Optional
    private String buyerId;
    private String sellerEmail;
    private String buyerEmail;
    private String sellerAccountName;
    private String buyerAccountName;
    private Double price; // combine with totalFee
    private Integer quantity; // combine with totalFee
    private String body;
    private String showUrl;
    private String paymethod;
    private String enablePaymethod;
    private Boolean needCtuCheck;
    private String royaltyType;
    private String royaltyParameters;
    private String antiPhishingKey;
    private String exterInvokeIp;
    private String extraCommonParam;
    private String extendParam;
    private String itBPay;
    private Boolean defaultLogin;
    private String productType;
    private String token;
    private String itemOrdersInfo;
    private String signIdExt;
    private String signNameExt;
    private Integer qrPayMode;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getErrorNotifyUrl() {
        return errorNotifyUrl;
    }

    public void setErrorNotifyUrl(String errorNotifyUrl) {
        this.errorNotifyUrl = errorNotifyUrl;
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

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
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

    public String getSellerAccountName() {
        return sellerAccountName;
    }

    public void setSellerAccountName(String sellerAccountName) {
        this.sellerAccountName = sellerAccountName;
    }

    public String getBuyerAccountName() {
        return buyerAccountName;
    }

    public void setBuyerAccountName(String buyerAccountName) {
        this.buyerAccountName = buyerAccountName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getEnablePaymethod() {
        return enablePaymethod;
    }

    public void setEnablePaymethod(String enablePaymethod) {
        this.enablePaymethod = enablePaymethod;
    }

    public Boolean getNeedCtuCheck() {
        return needCtuCheck;
    }

    public void setNeedCtuCheck(Boolean needCtuCheck) {
        this.needCtuCheck = needCtuCheck;
    }

    public String getRoyaltyType() {
        return royaltyType;
    }

    public void setRoyaltyType(String royaltyType) {
        this.royaltyType = royaltyType;
    }

    public String getRoyaltyParameters() {
        return royaltyParameters;
    }

    public void setRoyaltyParameters(String royaltyParameters) {
        this.royaltyParameters = royaltyParameters;
    }

    public String getAntiPhishingKey() {
        return antiPhishingKey;
    }

    public void setAntiPhishingKey(String antiPhishingKey) {
        this.antiPhishingKey = antiPhishingKey;
    }

    public String getExterInvokeIp() {
        return exterInvokeIp;
    }

    public void setExterInvokeIp(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    public String getItBPay() {
        return itBPay;
    }

    public void setItBPay(String itBPay) {
        this.itBPay = itBPay;
    }

    public Boolean getDefaultLogin() {
        return defaultLogin;
    }

    public void setDefaultLogin(Boolean defaultLogin) {
        this.defaultLogin = defaultLogin;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getItemOrdersInfo() {
        return itemOrdersInfo;
    }

    public void setItemOrdersInfo(String itemOrdersInfo) {
        this.itemOrdersInfo = itemOrdersInfo;
    }

    public String getSignIdExt() {
        return signIdExt;
    }

    public void setSignIdExt(String signIdExt) {
        this.signIdExt = signIdExt;
    }

    public String getSignNameExt() {
        return signNameExt;
    }

    public void setSignNameExt(String signNameExt) {
        this.signNameExt = signNameExt;
    }

    public Integer getQrPayMode() {
        return qrPayMode;
    }

    public void setQrPayMode(Integer qrPayMode) {
        this.qrPayMode = qrPayMode;
    }

}
