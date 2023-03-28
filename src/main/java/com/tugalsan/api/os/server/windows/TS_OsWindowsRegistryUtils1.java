package com.tugalsan.api.os.server.windows;

import com.tugalsan.api.os.server.TS_OsProcess;
import com.tugalsan.api.string.client.*;

public class TS_OsWindowsRegistryUtils1 {

    public static String readRegistry(CharSequence location, CharSequence key) {
        var cmd = TGS_StringUtils.concat("reg query ", "\"", location, "\" /v ", key);
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
