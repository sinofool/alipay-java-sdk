package net.sinofool.alipay.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupStringPair {
    public static GroupStringPair parseQueryString(final String queryString, final String enc)
            throws UnsupportedEncodingException {
        GroupStringPair ret = new GroupStringPair();
        if (queryString == null || queryString.isEmpty()) {
            return ret;
        }
        StringBuilder key = new StringBuilder();
        StringBuilder value = null;
        boolean isKey = true;
        for (char x : queryString.toCharArray()) {
            if ('&' == x) {
                if (!key.toString().isEmpty()) {
                    ret.add(URLDecoder.decode(key.toString(), enc), value == null ? null : URLDecoder.decode(value.toString(), enc));
                }
                key = new StringBuilder();
                value = null;
                isKey = true;
            } else if ('=' == x) {
                isKey = false;
                value = new StringBuilder();
            } else {
                (isKey ? key : value).append(x);
            }
        }
        if (!key.toString().isEmpty()) {
            ret.add(URLDecoder.decode(key.toString(), enc), URLDecoder.decode(value.toString(), enc));
        }
        return ret;
    }

    public static GroupStringPair parsePostBody(final InputStream postBody, final String charset, final String enc) throws IOException {
        GroupStringPair ret = new GroupStringPair();
        if (postBody == null) {
            return ret;
        }
        InputStreamReader reader = new InputStreamReader(postBody, charset);
        StringBuilder key = new StringBuilder();
        StringBuilder value = null;
        boolean isKey = true;
        char[] buff = new char[1024];
        int size = reader.read(buff);
        while (size != -1) {
            for (int i = 0; i < size; ++i) {
                if ('&' == buff[i]) {
                    if (!key.toString().isEmpty()) {
                        ret.add(URLDecoder.decode(key.toString(), enc), value == null ? null : URLDecoder.decode(value.toString(), enc));
                    }
                    key = new StringBuilder();
                    value = null;
                    isKey = true;
                } else if ('=' == buff[i]) {
                    isKey = false;
                    value = new StringBuilder();
                } else {
                    (isKey ? key : value).append(buff[i]);
                }
            }
            size = reader.read(buff);
        }
        if (!key.toString().isEmpty()) {
            ret.add(URLDecoder.decode(key.toString(), enc), URLDecoder.decode(value.toString(), enc));
        }
        return ret;
    }

    private List<StringPair> entries = new ArrayList<StringPair>();

    public void add(String key, String value) {
        entries.add(new StringPair(key, value));
    }

    public String get(String key) {
        for (StringPair entry : entries) {
            if (entry.getFirst().equals(key)) {
                return entry.getSecond();
            }
        }
        return null;
    }

    public void update(String key, String value) {
        for (StringPair entry : entries) {
            if (entry.getFirst().equals(key)) {
                entry.setSecond(value);
            }
        }
    }

    public List<StringPair> getOrdered(String... skipKeys) {
        Arrays.sort(skipKeys);
        List<StringPair> ret = new ArrayList<StringPair>();
        for (StringPair entry : entries) {
            if (Arrays.binarySearch(skipKeys, entry.getFirst()) >= 0) {
                continue;
            }
            ret.add(new StringPair(entry.getFirst(), entry.getSecond()));
        }
        return ret;
    }

    public List<StringPair> getSorted(String... skipKeys) {
        List<StringPair> ret = getOrdered(skipKeys);
        Collections.sort(ret);
        return ret;
    }
}
