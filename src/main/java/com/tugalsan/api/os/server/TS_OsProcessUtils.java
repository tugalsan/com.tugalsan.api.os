package com.tugalsan.api.os.server;

import module com.tugalsan.api.function;
import module com.tugalsan.api.string;
import module com.tugalsan.api.union;
import java.io.*;
import java.lang.ProcessHandle.*;
import java.nio.file.*;
import java.util.*;
import java.util.prefs.*;
import java.util.stream.*;

public class TS_OsProcessUtils {

    public static TGS_UnionExcuse<List<String>> procesesListJavaCommandlines() {
        var cmd = "wmic PROCESS where \"name like '%java%'\" get Commandline";
        var p = TS_OsProcess.of(cmd);
        if (p.exception != null) {
            return TGS_UnionExcuse.ofExcuse(p.exception);
        }
        return TGS_UnionExcuse.of(TGS_StringUtils.jre().toList(p.output, "\n"));
    }

    public static TGS_UnionExcuse<String> procesesByJarName_getProcessId(String jarName) {
        var cmd = "wmic PROCESS where \"name like '%java.exe%' AND CommandLine like '%" + jarName + "%'\"get Processid";
        var p = TS_OsProcess.of(cmd);
        if (p.exception != null) {
            return TGS_UnionExcuse.ofExcuse(p.exception);
        }
        return TGS_UnionExcuse.of(p.output);
    }

    public static TGS_UnionExcuse<String> procesesByJarName_killProcess(String jarName) {
        var cmd = "wmic PROCESS Where \"name Like '%java.exe%' AND CommandLine like '%" + jarName + "%'\" Call Terminate";
        var p = TS_OsProcess.of(cmd);
        if (p.exception != null) {
            return TGS_UnionExcuse.ofExcuse(p.exception);
        }
        return TGS_UnionExcuse.of(p.output);
    }

    public static boolean isRunningAsAdministrator() {
        synchronized (System.err) {
            try {
                var pref = Preferences.systemRoot();
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

    @Deprecated //NOT WORKING PROPERLY, USE TS_OsProcess.of
    public static TGS_UnionExcuseVoid runJar(Path jarFile, List<CharSequence> arguments) {
        return runJar(jarFile, arguments.stream().toArray(CharSequence[]::new));
    }

    @Deprecated //NOT WORKING PROPERLY, USE TS_OsProcess.of
    public static TGS_UnionExcuseVoid runJar(Path jarFile, CharSequence... arguments) {
        return TGS_FuncMTCUtils.call(() -> {
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

    public static void addShutdownHook(TGS_FuncMTU run) {
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
//    public static void addShutdownSignal(TGS_FuncMTU run) {
//        jdk.internal.misc.Signal.handle(new Signal("INT"), signal -> run.run());
//    }
}
