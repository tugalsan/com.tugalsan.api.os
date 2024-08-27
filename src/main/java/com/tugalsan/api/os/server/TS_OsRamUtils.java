package com.tugalsan.api.os.server;

import com.tugalsan.api.string.client.TGS_StringUtils;
import java.util.StringJoiner;

public class TS_OsRamUtils {

    public static void freeIt() {
        System.gc();
    }

    public static double getParameterMaxMemoryInMB() {
        return bytesToMiB(Runtime.getRuntime().maxMemory());
    }

    public static double getUsedMemoryInMB() {
        return bytesToMiB(Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory());
    }

    public static double getJVMReservableMemoryInMB() {
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
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getTotalMemoryInMB: ", String.format("%.2f", getJVMReservableMemoryInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getMaxMemoryInMB  : ", String.format("%.2f", getParameterMaxMemoryInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getFreeMemoryInMB : ", String.format("%.2f", getFreeMemoryInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getUsedMemoryInMB : ", String.format("%.2f", getUsedMemoryInMB())));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }
}
