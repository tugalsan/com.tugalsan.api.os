package com.tugalsan.api.os.server;

import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.lang.ProcessHandle.Info;
import java.nio.file.Path;
import java.util.List;
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
            var java = ProcessHandle.current().info().command().get();
//            d.ci("main", "cmd", java);
            var pre = "--enable-preview --add-modules jdk.incubator.concurrent -jar ";
            var jar = Path.of("C:\\me\\codes\\com.tugalsan\\trm\\com.tugalsan.trm.trainer\\target\\com.tugalsan.trm.trainer-1.0-SNAPSHOT-jar-with-dependencies.jar");
            var args = List.of("arg0", "arg1");
            var cmd = pre + "\"" + jar.toAbsolutePath().toString() + "\" " + String.join(" ", args);
//            d.ci("main", "cmd", cmd);
            var pb = new ProcessBuilder(java, cmd);
            pb.start();
            return true;
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }
}
