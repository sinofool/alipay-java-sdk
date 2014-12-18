package net.sinofool.alipay.base;

public class StringPair {
    private final String first;
    private String second;

    public StringPair(final String first, final String second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getSecond() {
        return second;
    }

}
