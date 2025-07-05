package org.ojx.utils;

public final class Time {
    public static final long now() {
        return System.currentTimeMillis() / 1000;
    }
}