package com.tugalsan.api.os.server;

public class TS_ProcessorUtils {

    public static int count() {
        return Runtime.getRuntime().availableProcessors();
    }
}
