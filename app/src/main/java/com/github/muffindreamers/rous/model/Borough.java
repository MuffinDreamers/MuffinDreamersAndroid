package com.github.muffindreamers.rous.model;

import java.io.Serializable;

/**
 * Created by Brooke on 10/16/2017.
 */

public enum Borough implements Serializable {
    MANHATTAN("Manhattan"),
    STATEN_ISLAND("Staten Island"),
    QUEENS("Queens"),
    BROOKLYN("Brooklyn"),
    BRONX("Bronx");

    private final String abrv;

    private Borough(final String ab) {
        abrv = ab;
    }

    @Override
    public String toString() {
        return abrv;
    }
}
