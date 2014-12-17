package net.sinofool.alipay.base;

import java.io.Serializable;
import java.util.Comparator;

public class StringPairComparator implements Comparator<StringPair>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(StringPair o1, StringPair o2) {
        return o1.first().compareTo(o2.first());
    }

}
