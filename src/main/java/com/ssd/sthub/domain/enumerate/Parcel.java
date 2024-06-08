package com.ssd.sthub.domain.enumerate;

public enum Parcel {

    CJ("04"), POSTOFFICE("01"), HANJIN("05"), ROZEN("06"), LOTTE("08"), CU("46");

    private final String code;

    Parcel(String code) {
        this.code = code;
    }

    public final String getCode() {
        return this.code;
    }
}
