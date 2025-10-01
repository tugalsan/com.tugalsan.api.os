package com.tugalsan.api.os.server;

import module com.tugalsan.api.string;
import java.util.*;

public class TS_OsRamUtils {

    public static void freeIt() {
        System.gc();
    }

    public static double getPercentageUsed() {
        return 100 * getUsedMemoryInMB() / getTotalMemoryMaxInMB();
    }

    public static double getTotalMemoryMaxInMB() {
        return bytesToMiB(Runtime.getRuntime().maxMemory());
    }

    public static double getFreeMemoryMaxInMB() {
        return getTotalMemoryMaxInMB() - getUsedMemoryInMB();
    }

    public static double getTotalMemoryCurInMB() {
        return bytesToMiB(Runtime.getRuntime().totalMemory());
    }

    public static double getFreeMemoryCurInMB() {
        return bytesToMiB(Runtime.getRuntime().freeMemory());
    }

    public static double getUsedMemoryInMB() {
        return bytesToMiB(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
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
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getTotalMemoryMaxInMB: ", String.format("%.1f", getTotalMemoryMaxInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getFreeMemoryMaxInMB : ", String.format("%.1f", getFreeMemoryMaxInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getTotalMemoryCurInMB: ", String.format("%.1f", getTotalMemoryCurInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getFreeMemoryCurInMB : ", String.format("%.1f", getFreeMemoryCurInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getUsedMemoryInMB    : ", String.format("%.1f", getUsedMemoryInMB())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName + ".getPercentageUsed    : ", String.format("%.1f", getPercentageUsed())));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }
}
