package com.tugalsan.api.os.server.windows;

import module com.sun.jna.platform;
import java.util.*;
import java.nio.file.*;

public class TS_OsWindowsWallpaper {

    public static void set(Path file) {
        SPI.INSTANCE.SystemParametersInfo(
                new WinDef.UINT_PTR(SPI.SPI_SETDESKWALLPAPER),
                new WinDef.UINT_PTR(0),
                file.toString(),
                new WinDef.UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
    }

    private interface SPI extends StdCallLibrary {

        long SPI_SETDESKWALLPAPER = 20;
        long SPIF_UPDATEINIFILE = 0x01;
        long SPIF_SENDWININICHANGE = 0x02;
        SPI INSTANCE = (SPI) Native.load("user32", SPI.class, new HashMap() {
            {
                put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
                put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
            }
        });

        boolean SystemParametersInfo(
                WinDef.UINT_PTR uiAction,
                WinDef.UINT_PTR uiParam,
                String pvParam,
                WinDef.UINT_PTR fWinIni
        );
    }
}
