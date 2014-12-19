package net.sinofool.alipay.dict;

public class AlipayPCRequestDict {
    // Required
    public class REQUIRED_SYS {
        public static final String SERVICE = "service";
        public static final String PARTNER = "partner";
        public static final String INPUT_CHARSET = "_input_charset";
        public static final String SIGN_TYPE = "sign_type";
        public static final String SIGN = "sign";
    }

    public class OPTIONAL_SYS {
        // Optional
        public static final String NOTIFY_URL = "notify_url";
        public static final String RETURN_URL = "return_url";
        public static final String ERROR_NOTIFY_URL = "error_notify_url";
    }

    public class REQUIRED_TRADE {
        // Required
        public static final String OUT_TRADE_NO = "out_trade_no";
        public static final String SUBJECT = "subject";
        public static final String PAYMENT_TYPE = "payment_type";
        public static final String TOTAL_FEE = "total_fee";
        public static final String SELLER_ID = "seller_id";
    }

    public class OPTIONAL_TRADE {
        public static final String BUYER_ID = "buyer_id";
        public static final String SELLER_EMAIL = "seller_email";
        public static final String BUYER_EMAIL = "buyer_email";
        public static final String SELLER_ACCOUNT_NAME = "seller_account_name";
        public static final String BUYER_ACCOUNT_NAME = "buyer_account_name";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String BODY = "body";
        public static final String SHOW_URL = "show_url";
        public static final String PAYMETHOD = "paymethod";
        public static final String ENABLE_PAYMETHOD = "enable_paymethod";
        public static final String NEED_CTU_CHECK = "need_ctu_check";
        public static final String ROYALTY_TYPE = "royalty_type";
        public static final String ROYALTY_PARAMETERS = "royalty_parameters";
        public static final String ANTI_PHISHING_KEY = "anti_phishing_key";
        public static final String EXTER_INVOKE_IP = "exter_invoke_ip";
        public static final String EXTRA_COMMON_PARAM = "extra_common_param";
        public static final String EXTEND_PARAM = "extend_param";
        public static final String IT_B_PAY = "it_b_pay";
        public static final String DEFAULT_LOGIN = "default_login";
        public static final String PRODUCT_TYPE = "product_type";
        public static final String TOKEN = "token";
        public static final String ITEM_ORDERS_INFO = "item_orders_info";
        public static final String SIGN_ID_EXT = "sign_id_ext";
        public static final String SIGN_NAME_EXT = "sign_name_ext";
        public static final String QR_PAY_MODE = "qr_pay_mode";
    }
}
