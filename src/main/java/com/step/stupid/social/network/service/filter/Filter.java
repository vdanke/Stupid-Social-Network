package com.step.stupid.social.network.service.filter;

import java.util.stream.Stream;

public abstract class Filter<T> {

    private boolean isActive;

    protected Filter(boolean isActive) {
        this.isActive = isActive;
    }

    public abstract Stream<T> filter(Stream<T> t);

    public boolean isActive() {
        return isActive;
    }
}
