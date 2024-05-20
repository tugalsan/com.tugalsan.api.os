package com.tugalsan.api.os.server;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
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

    public static TGS_UnionExcuseVoid runJar(Path jarFile, List<CharSequence> arguments) {
        return runJar(jarFile, arguments.stream().toArray(CharSequence[]::new));
    }

    public static TGS_UnionExcuseVoid runJar(Path jarFile, CharSequence... arguments) {
        return TGS_UnSafe.call(() -> {
            var java = ProcessHandle.current().info().command().get();
//            d.ci("main", "cmd", java);
            var pre = "--enable-preview --add-modules jdk.incubator.vector -jar ";
            var cmd = pre + "\"" + jarFile.toAbsolutePath().toString() + "\" " + String.join(" ", arguments);
//            d.ci("main", "cmd", cmd);
            var pb = new ProcessBuilder(java, cmd);
            pb.start();
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static void addShutdownHook(TGS_Runnable run) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Thread.sleep(200);
                    run.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });
    }
//
//    @Deprecated
//    public static void addShutdownSignal(TGS_Runnable run) {
//        jdk.internal.misc.Signal.handle(new Signal("INT"), signal -> run.run());
//    }
}
