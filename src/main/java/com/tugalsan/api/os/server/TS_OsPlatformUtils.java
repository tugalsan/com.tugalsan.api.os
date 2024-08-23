package com.tugalsan.api.os.server;

import com.sun.jna.Platform;
import com.tugalsan.api.cast.client.TGS_CastUtils;
import java.nio.file.*;
import java.util.*;
import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.stream.client.*;
import com.tugalsan.api.string.client.*;

public class TS_OsPlatformUtils {

    public static boolean platformWindows() {
        return Platform.isWindows();
    }

    public static boolean platformAndroid() {
        return Platform.isAndroid();
    }

    public static boolean platformLinux() {
        return Platform.isLinux();
    }

    public static String getName() {
        return TGS_CharSetCast.current().toLowerCase(System.getProperty("os.name"));
    }

    public static String getVersion() {
        return TGS_CharSetCast.current().toLowerCase(System.getProperty("os.version"));
    }

    public static Double getVersionNumber() {
        var verStr = getVersion();
        if (verStr == null) {
            return null;
        }
        if (TGS_CastUtils.isDouble(verStr)) {
            return TGS_CastUtils.toDouble(verStr);
        }
        var alphaIdx = -1;
        for (var i = 0; i < verStr.length(); i++) {
            if (TGS_CastUtils.isInteger(verStr.charAt(i) + "")) {
                continue;
            }
            alphaIdx = i;
            break;
        }
        verStr = verStr.substring(0, alphaIdx);
        if (verStr.isEmpty()) {
            return null;
        }
        return TGS_CastUtils.toDouble(verStr);
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
        sb.add(TGS_StringUtils.cmn().concat("TK_OSUtils.getName: ", getName()));
        sb.add(TGS_StringUtils.cmn().concat("TK_OSUtils.getVersion: ", getVersion()));
        sb.add(TGS_StringUtils.cmn().concat("TK_OSUtils.isWindows: ", String.valueOf(isWindows())));
        sb.add(TGS_StringUtils.cmn().concat("TK_OSUtils.isLinux: ", String.valueOf(isLinux())));
        sb.add(TGS_StringUtils.cmn().concat("TK_OSUtils.getPathAPI: ", getPathAPI().toString()));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }

//    public static void main(String... s) {
//        var a = toStringAll(true, true);
//        System.out.println(a);
//    }
    //PRIVATE
    private static List<Path> toPaths(CharSequence list, CharSequence delimiter) {
        return TGS_StreamUtils.toLst(
                Arrays.stream(list.toString().split(delimiter.toString()))
                        .map(split -> Path.of(split))
        );
    }
}
