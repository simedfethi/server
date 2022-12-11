package com.avanquest.scibscrm.domain.enumeration;

/**
 * The TransactionSource enumeration.
 */
public enum TransactionSource {
    APPEL,
    EMAIL,
    SALON,
    AUTRES,
    CLIENT_EXISTANT("Client existant"),
    RECOMMANDATION,
    WEBSITE;

    private String value;

    TransactionSource() {}

    TransactionSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
