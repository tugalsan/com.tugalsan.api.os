package com.tugalsan.api.os.server;

import com.tugalsan.api.function.client.TGS_Func;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ProcessHandle.Info;
import java.nio.file.Path;
import java.util.List;
import java.util.prefs.BackingStoreException;
import static java.util.prefs.Preferences.systemRoot;
import java.util.stream.Stream;

public class TS_OsProcessUtils {

    public static boolean isRunningAsAdministrator() {
        synchronized (System.err) {
            try {
                var pref = systemRoot();
                System.setErr(new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                    }
                }));
                pref.put("foo", "bar"); // SecurityException on Windows
                pref.remove("foo");
                pref.flush(); // BackingStoreException on Linux
                return true;
            } catch (BackingStoreException exception) {
                return false;
            } finally {
                System.setErr(System.err);
            }
        }
    }

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

    public static TS_OsProcess serviceStart(String serviceName) {
        if (!TS_OsPlatformUtils.isWindows()) {
            throw new UnsupportedOperationException(TS_OsProcessUtils.class.getSimpleName() + ".serviceStart not implemented yet for os other than windows.");
        }
        return TS_OsProcess.of("cmd.exe", "/c", "sc", "start", serviceName);
    }

    public static TS_OsProcess serviceStop(String serviceName) {
        if (!TS_OsPlatformUtils.isWindows()) {
            throw new UnsupportedOperationException(TS_OsProcessUtils.class.getSimpleName() + ".serviceStart not implemented yet for os other than windows.");
        }
        return TS_OsProcess.of("cmd.exe", "/c", "sc", "stop", serviceName);
    }

    public static TS_OsProcess serviceInfo(String serviceName) {
        if (!TS_OsPlatformUtils.isWindows()) {
            throw new UnsupportedOperationException(TS_OsProcessUtils.class.getSimpleName() + ".serviceStart not implemented yet for os other than windows.");
        }
        return TS_OsProcess.of("cmd.exe", "/c", "sc", "query", serviceName, "|", "find", "/C", "\"RUNNING\"");
    }

//    public static TGS_UnionExcuse<ProcessBuilder> runJar(boolean inheritIO, Path jarFile, List<CharSequence> arguments) {
//        return runJar(inheritIO, jarFile, arguments.stream().toArray(CharSequence[]::new));
//    }
//
//    public static TGS_UnionExcuse<ProcessBuilder> runJar(boolean inheritIO, Path jarFile, CharSequence... arguments) {
    public static TGS_UnionExcuse<ProcessBuilder> runJar(boolean inheritIO, CharSequence file_and_arguments) {
        return TGS_UnSafe.call(() -> {
            var java = ProcessHandle.current().info().command().get();
//            d.ci("main", "cmd", java);
            var pre = "--enable-preview --add-modules jdk.incubator.vector -jar ";
//            var cmd = pre + "\"" + jarFile.toAbsolutePath().toString() + "' " + String.join(" ", arguments);
            var cmd = pre + file_and_arguments;
//            d.ci("main", "cmd", cmd);
            var pb = new ProcessBuilder(java + " " + cmd);
            if (inheritIO) {
                pb.inheritIO();
            }
            pb.start();
            return TGS_UnionExcuse.of(pb);
        }, e -> {
            return TGS_UnionExcuse.ofExcuse(e);
        });
    }

    public static void addShutdownHook(TGS_Func run) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
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
//    public static void addShutdownSignal(TGS_Func run) {
//        jdk.internal.misc.Signal.handle(new Signal("INT"), signal -> run.run());
//    }
}
