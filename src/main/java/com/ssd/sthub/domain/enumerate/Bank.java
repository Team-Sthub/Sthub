package com.ssd.sthub.domain.enumerate;

public enum Bank {
    SHINHAN("shinhan"), HANA("hana"), KOOKMIN("KB"), WOORI("woori"), NONGHYUP("NH");

    private String value;

    Bank(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
