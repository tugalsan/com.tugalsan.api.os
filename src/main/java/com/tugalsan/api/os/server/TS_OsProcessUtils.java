package com.tugalsan.api.os.server;

import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.lang.ProcessHandle.Info;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TS_OsProcessUtils {

    public static int processorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static long pid() {
        return ProcessHandle.current().pid();
    }

    public static Info info() {
        return ProcessHandle.current().info();
    }

    public static Stream<ProcessHandle> list() {
        return ProcessHandle.allProcesses();
    }

    public static boolean runJar(Path jarFile, CharSequence... arguments) {
        return TGS_UnSafe.call(() -> {
            var pre = "--enable-preview --add-modules jdk.incubator.concurrent -jar ";
            var pb = new ProcessBuilder(info().command().get(), pre + "\"" + jarFile.toAbsolutePath().toString() + "\" " + String.join(" ", arguments));
            pb.inheritIO().start();
            return true;
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }
}
