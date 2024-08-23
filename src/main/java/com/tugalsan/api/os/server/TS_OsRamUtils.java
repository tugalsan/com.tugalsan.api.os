package com.tugalsan.api.os.server;

import com.tugalsan.api.string.client.TGS_StringUtils;
import java.util.StringJoiner;

public class TS_OsRamUtils {

    public static void freeIt() {
        System.gc();
    }

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

    public static String toStringAll(boolean hrStart, boolean hrEnd) {
        var ClassName = TS_OsRamUtils.class.getSimpleName();
        var sb = new StringJoiner("\n");
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getMaxMemoryInMB  : ", String.format("%3.2f", getMaxMemoryInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getUsedMemoryInMB : ", String.format("%3.2f", getUsedMemoryInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getTotalMemoryInMB: ", String.format("%3.2f", getTotalMemoryInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getFreeMemoryInMB : ", String.format("%3.2f", getFreeMemoryInMB())));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }
}
