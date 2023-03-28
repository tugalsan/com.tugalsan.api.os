package com.tugalsan.api.os.server;

public class TS_OsProcessUtils {

    public static int processorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static long pid() {
        return ProcessHandle.current().pid();
    }
}
