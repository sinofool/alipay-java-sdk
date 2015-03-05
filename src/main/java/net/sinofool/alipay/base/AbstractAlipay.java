package net.sinofool.alipay.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

import net.sinofool.alipay.AlipayConfig;
import net.sinofool.alipay.AlipayException;

public abstract class AbstractAlipay {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AbstractAlipay.class);
    private static final Charset CHARSET = Charset.forName("utf-8");

    protected AlipayConfig config;
    private final PrivateKey myPrivateKey;
    private final PublicKey alipayPublicKey;
    protected final boolean preferRSA;

    protected AbstractAlipay(final AlipayConfig config) {
        this.config = config;
        myPrivateKey = initMyPrivateKey(config.getMyPrivateKey());
        alipayPublicKey = initAlipayPublicKey(config.getAlipayPublicKey());
        preferRSA = (myPrivateKey != null && alipayPublicKey != null);
    }

    private PublicKey initAlipayPublicKey(String key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = DatatypeConverter.parseBase64Binary(key);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            return pubKey;
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot initialzie RSA public key, fallback to md5", e);
        } catch (InvalidKeySpecException e) {
            LOG.warn("Cannot initialzie RSA public key, fallback to md5", e);
        }
        return null;
    }

    private PrivateKey initMyPrivateKey(String key) {
        try {
            byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot initialzie RSA private key, fallback to md5", e);
        } catch (InvalidKeySpecException e) {
            LOG.warn("Cannot initialzie RSA private key, fallback to md5", e);
        }
        return null;
    }

    protected String signMD5(final List<StringPair> p) {
        String param = join(p, false, false);
        String sign = md5Sign(param + config.getMD5KEY());
        LOG.trace("Signing {}", param);
        return sign;
    }

    protected String signRSA(final List<StringPair> p) {
        String param = join(p, false, false);
        String sign = rsaSign(param);
        return sign;
    }

    /**
     * Mobile SDK join the fields with quote, which is not documented at all.
     * @param p
     * @return
     */
    protected String signRSAWithQuote(final List<StringPair> p) {
        String param = join(p, false, true);
        String sign = rsaSign(param);
        return sign;
    }

    protected boolean verifyRSA(String sign, List<StringPair> p) {
        String param = join(p, false, false);
        LOG.trace("verifyRSA sing={}", sign);
        LOG.trace("verifyRSA content={}" + param);
        return rsaVerify(param, sign);
    }

    protected boolean verifyMD5(String sign, List<StringPair> p) {
        return sign.equals(signMD5(p));
    }

    protected String join(final List<StringPair> p, boolean encode, boolean quote) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < p.size(); ++i) {
            if (i != 0) {
                buff.append("&");
            }
            buff.append(p.get(i).getFirst()).append("=");
            if (quote) {
                buff.append("\"");
            }
            if (encode) {
                try {
                    buff.append(URLEncoder.encode(p.get(i).getSecond(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.warn("Cannot encode {}", p.get(i).getSecond());
                    throw new AlipayException(e);
                }
            } else {
                buff.append(p.get(i).getSecond());
            }
            if (quote) {
                buff.append("\"");
            }
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

    private static String md5Sign(String input) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(input.getBytes(CHARSET));
            return hex(digest);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot find MD5 digest algorithm", e);
            throw new AlipayException(e);
        }
    }

    private String rsaSign(String content) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(myPrivateKey);
            signature.update(content.getBytes(CHARSET));
            byte[] signed = signature.sign();
            return DatatypeConverter.printBase64Binary(signed);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot sign content", e);
        } catch (InvalidKeyException e) {
            LOG.warn("Cannot sign content", e);
        } catch (SignatureException e) {
            LOG.warn("Cannot sign content", e);
        }
        return null;
    }

    protected boolean rsaVerify(String content, String sign) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(alipayPublicKey);
            signature.update(content.getBytes(CHARSET));

            boolean bverify = signature.verify(DatatypeConverter.parseBase64Binary(sign));
            return bverify;
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot verify signature", e);
        } catch (InvalidKeyException e) {
            LOG.warn("Cannot verify signature", e);
        } catch (SignatureException e) {
            LOG.warn("Cannot verify signature", e);
        }
        return false;
    }

    protected String decrypt(String content) {
        LOG.trace("decrypt content={}", content);
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, myPrivateKey);

            InputStream ins = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(content));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            // TODO change this value depends on length of key
            byte[] buf = new byte[256];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), CHARSET);
        } catch (NoSuchPaddingException e) {
            LOG.warn("Cannot decrypt content", e);
        } catch (InvalidKeyException e) {
            LOG.warn("Cannot decrypt content", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot decrypt content", e);
        } catch (IllegalBlockSizeException e) {
            LOG.warn("Cannot decrypt content", e);
        } catch (BadPaddingException e) {
            LOG.warn("Cannot decrypt content", e);
        } catch (IOException e) {
            LOG.warn("Cannot decrypt content", e);
        }
        return null;
    }

    public GroupStringPair parseQueryString(final String queryString) {
        try {
            return GroupStringPair.parseQueryString(queryString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Error parsing query string", e);
            throw new AlipayException(e);
        }
    }

    public GroupStringPair parsePostBody(final InputStream postBody) {
        try {
            return GroupStringPair.parsePostBody(postBody, "utf-8", "utf-8");
        } catch (IOException e) {
            LOG.warn("Error parsing post body", e);
            throw new AlipayException(e);
        }
    }

}
