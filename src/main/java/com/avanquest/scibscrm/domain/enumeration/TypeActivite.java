package com.avanquest.scibscrm.domain.enumeration;

/**
 * The TypeActivite enumeration.
 */
public enum TypeActivite {
    EMAIL,
    COMMENTAIRE,
    REUNION,
    APPELER,
    A_FAIRE("A FAIRE"),
    SMS,
    CONFERENCE,
    TACHE;

    private String value;

    TypeActivite() {}

    TypeActivite(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
