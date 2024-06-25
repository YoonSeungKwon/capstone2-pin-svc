package yoon.capstone2.svc.pinService.enums;

import lombok.Getter;

@Getter
public enum Method {

    CREDIT_CARD("신용카드"),
    DEBIT_CARD("체크카드"),
    BANK_TRANSFER("계좌이체"),
    CASH("현금"),
    ETC("기타");

    private final String method;

    Method(String method){
        this.method = method;
    }

    public static Method fromString(String method) {
        for (Method m : Method.values()) {
            if (m.method.equalsIgnoreCase(method)) {
                return m;
            }
        }
        return ETC;
    }
}
