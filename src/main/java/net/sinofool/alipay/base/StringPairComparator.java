package net.sinofool.alipay.base;

import java.util.Comparator;

public class StringPairComparator implements Comparator<StringPair> {

    @Override
    public int compare(StringPair o1, StringPair o2) {
        return o1.first().compareTo(o2.first());
    }

}
