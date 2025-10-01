package com.tugalsan.api.os.server;

import module com.tugalsan.api.string;
import java.nio.file.*;
import java.util.*;

public class TS_OsUserUtils {

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static Path getPathUser() {//ON UBUNTU
        return Path.of(System.getProperty("user.home"));
    }

    public static Path getPathCurrent() {//ON UBUNTU
        return Path.of(System.getProperty("user.dir"));
    }

    public static String toStringAll(boolean hrStart, boolean hrEnd) {
        var ClassName = TS_OsUserUtils.class.getSimpleName();
        var sb = new StringJoiner("\n");
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".getUserName: [", getUserName()));
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".getPathCurrent: [", getPathUser().toString()));
        sb.add(TGS_StringUtils.cmn().concat(ClassName, ".getPathCurrent: [", getPathCurrent().toString()));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }
}
