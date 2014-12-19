package net.sinofool.alipay.dict;

public class AlipayWapNotifyDict {
    public class REQUIRED_SYS {
        public static final String SERVICE = "service";
        public static final String V = "v";
        public static final String SEC_ID = "sec_id";
        public static final String SIGN = "sign";
        public static final String NOTIFY_DATA = "notify_data";
    }

    public class REQUIRED_NOTIFYDATA {
        public static final String PAYMENT_TYPE = "payment_type";
        public static final String SUBJECT = "subject";
        public static final String TRADE_NO = "trade_no";
        public static final String BUYER_EMAIL = "buyer_email";
        public static final String GMT_CREATE = "gmt_create";
        public static final String NOTIFY_TYPE = "notify_type";
        public static final String QUANTITY = "quantity";
        public static final String OUT_TRADE_NO = "out_trade_no";
        public static final String NOTIFY_TIME = "notify_time";
        public static final String SELLER_ID = "seller_id";
        public static final String TRADE_STATUS = "trade_status";
        public static final String IS_TOTAL_FEE_ADJUST = "is_total_fee_adjust";
        public static final String TOTAL_FEE = "total_fee";
        public static final String GMT_PAYMENT = "gmt_payment";
        public static final String SELLER_EMAIL = "seller_email";
        public static final String GMT_CLOSE = "gmt_close";
        public static final String PRICE = "price";
        public static final String BUYER_ID = "buyer_id";
        public static final String NOTIFY_ID = "notify_id";
        public static final String USE_COUPON = "use_coupon";
    }

    public class OPTIONAL_NOTIFYDATA {
        public static final String REFUND_STATUS = "refund_status";
        public static final String GMT_REFUND = "gmt_refund";
    }
}
