package com.tugalsan.api.os.server;

public class TS_OsRamUtils {

    public static double getMaxMemoryInMB() {
        return bytesToMiB(Runtime.getRuntime().maxMemory());
    }

    public static double getUsedMemoryInMB() {
        return bytesToMiB(Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory());
    }

    public static double getTotalMemoryInMB() {
        return bytesToMiB(Runtime.getRuntime().totalMemory());
    }

    public static double getFreeMemoryInMB() {
        return bytesToMiB(Runtime.getRuntime().freeMemory());
    }

    private static double bytesToMiB(long bytes) {
        return ((double) bytes / (1024L * 1024L));
    }
}
