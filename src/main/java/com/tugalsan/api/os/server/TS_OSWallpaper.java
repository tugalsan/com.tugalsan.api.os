package com.tugalsan.api.os.server;

import java.util.*;
import java.nio.file.*;
import com.sun.jna.Native;
import com.sun.jna.win32.*;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;

public class TS_OSWallpaper {

    public static void set(Path file) {
        SPI.INSTANCE.SystemParametersInfo(
                new UINT_PTR(SPI.SPI_SETDESKWALLPAPER),
                new UINT_PTR(0),
                file.toString(),
                new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
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
                UINT_PTR uiAction,
                UINT_PTR uiParam,
                String pvParam,
                UINT_PTR fWinIni
        );
    }
}
