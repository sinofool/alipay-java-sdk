package net.sinofool.alipay.demo;

import net.sinofool.alipay.PCDirectSDK;

import java.util.LinkedList;

public class DemoHelper {
    private static DemoHelper ourInstance = new DemoHelper();

    public static DemoHelper getInstance() {
        return ourInstance;
    }

    private LinkedList<String> logs = new LinkedList<>();
    private PCDirectSDK pcSdk = new PCDirectSDK(new DemoAlipayGenericConfig());

    private DemoHelper() {
    }

    public synchronized String getLog() {
        StringBuffer sb = new StringBuffer();
        for (String log : logs) {
            sb.append(log).append("<br />");
        }
        return sb.toString();
    }

    public synchronized void appendLog(String log) {
        logs.addFirst(log);
        if (logs.size() > 10) {
            logs.removeLast();
        }
    }

    public PCDirectSDK getPCSDK() {
        return pcSdk;
    }
}
