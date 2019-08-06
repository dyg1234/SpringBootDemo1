package com.hello.exception;

public class CodeException extends Exception {
    private boolean verifyPass;

    public boolean isVerifyPass() {
        return verifyPass;
    }

    public CodeException(String message, boolean verifyPass) {
        super(message);
        this.verifyPass = verifyPass;
    }
}
