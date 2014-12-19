package net.sinofool.alipay.base;

public class StringPair implements Comparable<StringPair> {
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

    @Override
    public int compareTo(StringPair o) {
        return first.compareTo(o.first);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof StringPair) {
            StringPair p = (StringPair) obj;
            return first.equals(p.first);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode();
    }
}
