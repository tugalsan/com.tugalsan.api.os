package com.tugalsan.api.os.server;

import java.nio.file.*;
import java.util.*;
import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.stream.client.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.unsafe.client.*;

public class TS_OsJavaUtils {

    private static List<Path> toPaths(CharSequence list, CharSequence delimiter) {
        return TGS_StreamUtils.toLst(
                Arrays.stream(list.toString().split(delimiter.toString()))
                        .map(split -> Path.of(split))
        );
    }

    public static String getVersion() {
        return TGS_CharSetCast.toLocaleLowerCase(System.getProperty("java.version"));
    }

    public static String getName() {
        return TGS_CharSetCast.toLocaleLowerCase(System.getProperty("java.vendor"));
    }

    public static Path getPathJava() {
        return Path.of(System.getProperty("java.home"), "bin", (TS_OsPlatformUtils.isWindows() ? "javaw.exe" : "java"));
    }

    public static List<Path> getPathAPI() {
        return toPaths(System.getProperty("java.class.path"), ";");
    }

    public static String toStringAll(boolean hrStart, boolean hrEnd) {
        var sb = new StringJoiner("\n");
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        sb.add(TGS_StringUtils.concat("TK_JavaUtils.getName: [", getName()));
        sb.add(TGS_StringUtils.concat("TK_JavaUtils.getVersion: [", getVersion()));
        sb.add(TGS_StringUtils.concat("TK_JavaUtils.getPathJava: [", getPathJava().toString()));
        sb.add(TGS_StringUtils.concat("TK_JavaUtils.getPathAPI: [", getPathAPI().toString()));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }

    public static Path getJarPath(Class main) {
        return TGS_UnSafe.call(() -> {

            var codeSource = main.getProtectionDomain().getCodeSource();
            System.out.println("codeSource:" + codeSource);
            return Path.of(codeSource.getLocation().toURI().getPath());
        });
    }
}
