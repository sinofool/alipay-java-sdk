package net.sinofool.alipay;

public interface AlipayHttpClient {
    String get(String host, int port, String schema, String uri);

    String post(String host, int port, String schema, String uri, String body);
}
