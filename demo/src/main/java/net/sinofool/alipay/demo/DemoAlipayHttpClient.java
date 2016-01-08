package net.sinofool.alipay.demo;

import net.sinofool.alipay.AlipayHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class DemoAlipayHttpClient implements AlipayHttpClient {

    private String response(HttpResponse response) throws IllegalStateException, IOException {
        StringBuffer buff = new StringBuffer();
        InputStream content = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String line = reader.readLine();
        while (line != null) {
            buff.append(line);
            line = reader.readLine();
        }
        return buff.toString();
    }

    @Override
    public String post(String host, int port, String schema, String uri, String body) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(uri);
            post.setHeader("Host", host);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new StringEntity(body));
            HttpResponse execute = client.execute(new HttpHost(host, port, schema), post);
            return response(execute);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(String host, int port, String schema, String uri) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(uri);
            HttpResponse execute = client.execute(new HttpHost(host, port, schema), get);
            return response(execute);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}