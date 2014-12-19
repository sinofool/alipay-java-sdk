package net.sinofool.alipay.dict;

public class AlipayWapRequestCreateDict {
    public class REQUIRED_SYS {
        public static final String SERVICE = "service";
        public static final String FORMAT = "format";
        public static final String V = "v";
        public static final String PARTNER = "partner";
        public static final String REQ_ID = "req_id";
        public static final String SEC_ID = "sec_id";
        public static final String SIGN = "sign";
        public static final String REQ_DATA = "req_data";
    }

    public class REQUIRED_REQDATA {
        public static final String SUBJECT = "subject";
        public static final String OUT_TRADE_NO = "out_trade_no";
        public static final String TOTAL_FEE = "total_fee";
        public static final String SELLER_ACCOUNT_NAME = "seller_account_name";
        public static final String CALL_BACK_URL = "call_back_url";
    }

    public class OPTIONAL_REQDATA {
        public static final String NOTIFY_URL = "notify_url";
        public static final String OUT_USER = "out_user";
        public static final String MERCHANT_URL = "merchant_url";
        public static final String PAY_EXPIRE = "pay_expire";
        public static final String AGENT_ID = "agent_id";
    }
}
