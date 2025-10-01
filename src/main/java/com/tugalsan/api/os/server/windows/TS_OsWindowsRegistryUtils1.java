package com.tugalsan.api.os.server.windows;

import module com.tugalsan.api.os;
import module com.tugalsan.api.string;

public class TS_OsWindowsRegistryUtils1 {

    public static String readRegistry(CharSequence location, CharSequence key) {
        var cmd = TGS_StringUtils.cmn().concat("reg query ", "\"", location, "\" /v ", key);
        return TS_OsProcess.of(cmd).output;
    }

    public static void main(String[] args) {
        // Sample usage
        var regLoc = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\" + "Explorer\\Shell Folders";
        var regKey = "Personal";
        var value = TS_OsWindowsRegistryUtils1.readRegistry(regLoc, regKey);
        System.out.println(value);
    }

}
