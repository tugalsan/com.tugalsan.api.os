package com.tugalsan.api.os.server;

import com.tugalsan.api.string.client.*;

public class TS_RegistryUtils1 {

    public static String readRegistry(CharSequence location, CharSequence key) {
        var cmd = TGS_StringUtils.concat("reg query " , "\"" , location , "\" /v " , key);
        return TS_RuntimeUtils.runConsole_readResult(cmd);
    }

    public static void main(String[] args) {
        // Sample usage
        var regLoc = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\" + "Explorer\\Shell Folders";
        var regKey = "Personal";
        var value = TS_RegistryUtils1.readRegistry(regLoc, regKey);
        System.out.println(value);
    }

}
