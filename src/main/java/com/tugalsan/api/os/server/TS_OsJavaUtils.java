package com.tugalsan.api.os.server;

import module com.tugalsan.api.charset;
import module com.tugalsan.api.function;
import module com.tugalsan.api.stream;
import module com.tugalsan.api.string;
import module com.sun.jna.platform;
import java.nio.file.*;
import java.util.*;

public class TS_OsJavaUtils {

    private static List<Path> toPaths(CharSequence list, CharSequence delimiter) {
        return TGS_StreamUtils.toLst(
                Arrays.stream(list.toString().split(delimiter.toString()))
                        .map(split -> Path.of(split))
        );
    }

    public static String getVersion() {
        return TGS_CharSetCast.current().toLowerCase(System.getProperty("java.version"));
    }

    public static String getName() {
        return TGS_CharSetCast.current().toLowerCase(System.getProperty("java.vendor"));
    }

    public static Path getPathJava() {
        return Path.of(System.getProperty("java.home"), "bin", (TS_OsPlatformUtils.isWindows() ? "javaw.exe" : "java"));
    }

    public static List<Path> getPathAPI() {
        return toPaths(System.getProperty("java.class.path"), ";");
    }

    public static String toStringAll(boolean hrStart, boolean hrEnd) {
        var ClassName = TS_OsJavaUtils.class.getSimpleName();
        var sb = new StringJoiner("\n");
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".getName: ", getName()));
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".getVersion: ", getVersion()));
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".isWindows: ", String.valueOf(TS_OsPlatformUtils.isWindows())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".isLinux: ", String.valueOf(TS_OsPlatformUtils.isLinux())));
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".getPathAPI: ", getPathAPI().toString()));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }

    public static Path getJarPath() {
        return TGS_FuncMTCUtils.call(() -> {
            var codeSource = TS_OsJavaUtils.class.getProtectionDomain().getCodeSource();
//            System.out.println("codeSource:" + codeSource);
            var path = codeSource.getLocation().toURI().getPath();
//            System.out.println("path:" + path);
            if (Platform.isWindows()) {
//                System.out.println("isWindows");
                if (path.charAt(0) == '/') {
                    path = path.substring(1);
//                    System.out.println("path^:" + path);
//                } else {
//                    System.out.println("first char is not '/'. I is " + path.charAt(0));
                }
            }
            return Path.of(path);
        });
    }
}
