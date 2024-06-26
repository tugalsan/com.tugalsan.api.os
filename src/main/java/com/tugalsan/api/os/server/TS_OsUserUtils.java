package com.tugalsan.api.os.server;

import com.tugalsan.api.string.client.*;
import java.nio.file.Path;
import java.util.StringJoiner;

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
        var sb = new StringJoiner("\n");
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        sb.add(TGS_StringUtils.cmn().concat("TK_UserUtils.getUserName: [", getUserName()));
        sb.add(TGS_StringUtils.cmn().concat("TK_UserUtils.getPathCurrent: [", getPathUser().toString()));
        sb.add(TGS_StringUtils.cmn().concat("TK_UserUtils.getPathCurrent: [", getPathCurrent().toString()));
        if (hrStart) {
            sb.add("-----------------------------------------------------------------------------------");
        }
        return sb.toString();
    }
}
