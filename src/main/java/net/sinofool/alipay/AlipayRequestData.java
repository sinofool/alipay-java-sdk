package net.sinofool.alipay;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sinofool.alipay.base.OneLevelOnlyXML;
import net.sinofool.alipay.base.StringPair;

public class AlipayRequestData {
    private Map<String, String> data = new HashMap<String, String>();
    private Map<String, String> reqData = new HashMap<String, String>();

    public void setString(final String key, final String value) {
        data.put(key, value);
    }

    public void setReqString(final String key, final String value) {
        reqData.put(key, value);
    }

    public List<StringPair> getSortedParameters() {
        List<StringPair> ret = new LinkedList<StringPair>();
        String reqDataRootElement = null;
        for (Entry<String, String> param : data.entrySet()) {
            if ("req_data".equals(param.getKey())) {
                reqDataRootElement = param.getValue();
                continue;
            }
            ret.add(new StringPair(param.getKey(), param.getValue()));
        }

        if (reqDataRootElement != null) {
            OneLevelOnlyXML reqXML = new OneLevelOnlyXML();
            reqXML.createRootElement(reqDataRootElement);
            for (Entry<String, String> req : reqData.entrySet()) {
                reqXML.createChild(req.getKey(), req.getValue());
            }
            String reqData = reqXML.toXMLString();
            ret.add(new StringPair("req_data", reqData));
        }

        Collections.sort(ret);
        return ret;
    }
}
