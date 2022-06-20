package com.tugalsan.api.os.server;

import java.lang.management.*;
import java.nio.file.*;
import java.util.*;
import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.stream.client.*;
import com.tugalsan.api.string.client.*;

public class TS_OSUtils {

    public static int getRamInMB() {
        var ramInBytes = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize();
        var ramInKBytes = ramInBytes / 1024d;
        var ramInMBytes = ramInKBytes / 1024d;
        return (int) ramInMBytes;
    }

    public static String getName() {
        return TGS_CharacterSets.toLowerCase_TR(System.getProperty("os.name"));
    }

    public static String getVersion() {
        return TGS_CharacterSets.toLowerCase_TR(System.getProperty("os.version"));
    }

    public static boolean isWindows() {
        return getName().contains("win");
    }

    public static boolean isLinux() {
        return getName().contains("linux");
    }

    public static boolean isMac() {
        return getName().contains("mac");
    }

    public static boolean isSolaris() {
        return getName().contains("sunos");
    }

    public static boolean isUnix() {
        var name = getName();
        return name.contains("nix") || name.contains("nux") || name.contains("aix");
    }

    public static List<Path> getPathAPI() {
        return toPaths(System.getProperty("java.library.path"), ";");
    }

    public static String newLine() {
        return System.lineSeparator();
    }

    public static String toStringAll(boolean hrStart, boolean hrEnd) {
        var sb = new StringJoiner("\n");
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        sb.add(TGS_StringUtils.concat("TK_OSUtils.getRamInMB: ", String.valueOf(getRamInMB())));
        sb.add(TGS_StringUtils.concat("TK_OSUtils.getName: ", getName()));
        sb.add(TGS_StringUtils.concat("TK_OSUtils.getVersion: ", getVersion()));
        sb.add(TGS_StringUtils.concat("TK_OSUtils.isWindows: ", String.valueOf(isWindows())));
        sb.add(TGS_StringUtils.concat("TK_OSUtils.isLinux: ", String.valueOf(isLinux())));
        sb.add(TGS_StringUtils.concat("TK_OSUtils.getPathAPI: ", getPathAPI().toString()));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }

    public static void main(String... s) {
        var a = toStringAll(true, true);
        System.out.println(a);
    }

    //PRIVATE
    private static List<Path> toPaths(CharSequence list, CharSequence delimiter) {
        return TGS_StreamUtils.toList(
                Arrays.stream(list.toString().split(delimiter.toString()))
                        .map(split -> Path.of(split))
        );
    }
}
