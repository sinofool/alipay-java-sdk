package net.sinofool.alipay;

public class AlipayException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlipayException(Exception e) {
        super(e);
    }

    public AlipayException(String reason) {
        super(reason);
    }
}
