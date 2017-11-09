package com.github.muffindreamers.rous.model;

import java.io.Serializable;

/**
 * Creates enum for boroughs
 */
public enum Borough implements Serializable {
    MANHATTAN("Manhattan"),
    STATEN_ISLAND("Staten Island"),
    QUEENS("Queens"),
    BROOKLYN("Brooklyn"),
    BRONX("Bronx");

    /**
     * Created by Brooke on 10/16/2017.
     */
    private final String abbreviation;

    Borough(final String ab) {
        abbreviation = ab;
    }

    @Override
    public String toString() {
        return abbreviation;
    }
}
