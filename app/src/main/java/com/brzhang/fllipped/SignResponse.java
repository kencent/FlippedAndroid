package com.brzhang.fllipped;

public class SignResponse {
    private final String sig;

    public SignResponse(String sig) {
        this.sig = sig;
    }

    public String getSig() {
        return sig;
    }
}
