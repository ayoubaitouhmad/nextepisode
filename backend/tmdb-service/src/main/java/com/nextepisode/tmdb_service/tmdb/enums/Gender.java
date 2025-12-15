package com.nextepisode.tmdb_service.tmdb.enums;

public enum Gender {
    MALE(1),
    FEMALE(2),
    UNSPECIFIED(0);

    private final int dbValue;

    // Constructor for the enum constants
    Gender(int dbValue) {
        this.dbValue = dbValue;
    }

    // Getter method to retrieve the associated int value
    public int getDbValue() {
        return dbValue;
    }
}
