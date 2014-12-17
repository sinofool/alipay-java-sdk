package net.sinofool.alipay.base;

import java.util.Collections;
import java.util.List;

import net.sinofool.alipay.AlipayConfig;

import org.apache.commons.codec.digest.DigestUtils;

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
        String sign = DigestUtils.md5Hex(param + config.getMD5KEY());
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
}
