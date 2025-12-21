package com.nextepisode.tmdb_service.tmdb.enums;

import lombok.Getter;

@Getter
public enum ContentRuntime {
    SHORT(0),
    STANDARD(1),
    LONG(2);

    private final int runtime;

    ContentRuntime(int i) {
        this.runtime = i;
    }

    public String getName() {
        return super.name().toLowerCase();
    }
}
