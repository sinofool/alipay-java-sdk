package net.sinofool.alipay.base;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import net.sinofool.alipay.AlipayConfig;
import net.sinofool.alipay.AlipayException;

public abstract class AbstractAlipay {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AbstractAlipay.class);
    protected AlipayConfig config;

    protected AbstractAlipay(final AlipayConfig config) {
        this.config = config;
    }

    private void sortKeys(final List<StringPair> p) {
        Collections.sort(p, new StringPairComparator());
    }

    protected String signMD5(final List<StringPair> p) {
        sortKeys(p);
        String param = join(p);
        String sign = md5(param + config.getMD5KEY());
        LOG.trace("Signing {}", param);
        return sign;
    }

    protected String join(final List<StringPair> p) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < p.size(); ++i) {
            if (i != 0) {
                buff.append("&");
            }
            buff.append(p.get(i).first()).append("=").append(p.get(i).second());
        }
        return buff.toString();
    }

    // MD5 digest
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };

    private static String hex(byte[] input) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < input.length; j++) {
            buf.append(DIGITS[(input[j] >> 4) & 0x0f]);
            buf.append(DIGITS[input[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String md5(String input) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(input.getBytes(Charset.forName("utf-8")));
            return hex(digest);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot find MD5 digest algorithm", e);
            throw new AlipayException(e);
        }
    }
}
