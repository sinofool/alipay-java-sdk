package net.sinofool.alipay.base;

public class StringPair {
    private final String first;
    private final String second;

    public StringPair(final String first, final String second) {
        this.first = first;
        this.second = second;
    }

    public String first() {
        return first;
    }

    public String second() {
        return second;
    }

}
