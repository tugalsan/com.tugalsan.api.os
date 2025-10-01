package com.tugalsan.api.os.server;

import module com.tugalsan.api.list;
import module com.tugalsan.api.random;
import module com.tugalsan.api.string;
import module com.tugalsan.api.time;
import module com.tugalsan.api.function;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public class TS_OsProcess {

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(TS_OsProcess.class.getSimpleName()).append(" {");
        sb.append("\n   ").append("commandTokens {");
        Arrays.asList(commandTokens).forEach(ct -> {
            sb.append("\n   ").append("   ").append(ct);
        });
        sb.append("\n   ").append("}");
        sb.append("\n   ").append("pid=").append(pid);
        sb.append("\n   ").append("output=").append(output);
        sb.append("\n   ").append("error=").append(error);
        sb.append("\n   ").append("exception=").append(exception);
        sb.append("\n   ").append("exitValue=").append(exitValue);
        sb.append("\n   ").append("elapsed=").append(elapsed == null ? -1L : elapsed.toSeconds());
        sb.append("\n}");
        return sb.toString();
    }

    public static Path executionPath() {
        return Path.of(System.getProperty("user.dir"));
    }

    public static String constructJarExecuterString(CharSequence file, List<String> args) {
        var sb = new StringBuilder()
                .append("\"").append(TS_OsJavaUtils.getPathJava()).append("\"")
                .append(" -jar ")
                .append("\"").append(file).append("\" ");
        args.stream().forEachOrdered(s -> sb.append(" ").append(s));
        return sb.toString();
    }

    public static List<String> constructJarExecuterString_console_preview(CharSequence file, List<String> args) {
        List<String> args_out = new ArrayList();
        args_out.add("\"" + TS_OsJavaUtils.getPathJava().resolveSibling("java.exe") + "\"");
        args_out.add("--enable-preview");
//        args_out.add("-Djdk.jar.maxSignatureFileSize=800000000");
        args_out.add("-Xmx512m");
        args_out.add("-jar");
        args_out.add("\"" + file + "\" ");
        args_out.addAll(args);
        return args_out;
    }

    public static String constructJarExtractorString(CharSequence file) {
        return TGS_StringUtils.cmn().concat("jar xf ", file);
    }

    public static String constructJarFileListString(CharSequence file) {
        return TGS_StringUtils.cmn().concat("jar tf ", file);
    }
    public String[] commandTokens;
    public Process process;
    public long pid;
    public String output;
    public String error;
    public Exception exception;
    public int exitValue;
    public Duration elapsed;

    public boolean exitValueOk() {
        return exitValue == 0;
    }

    public static enum CodeType {
        BAT,
        VBS
    }

    private void process() {
        var sjOut = new StringJoiner("\n");
        var sjErr = new StringJoiner("\n");
        var _elapsed = TS_TimeElapsed.of();
        TGS_FuncMTCUtils.run(() -> {
            this.pid = process.pid();
            process.waitFor();
            try (var is = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                is.lines().forEach(line -> {
                    System.out.println(line);
                    sjOut.add(line);
                });
            }
            try (var es = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                es.lines().forEach(line -> {
                    System.err.println(line);
                    sjErr.add(line);
                });
            }
        }, e -> {
            this.exception = e;
        });
        if (process.pid() != 0) {
            this.pid = process.pid();
        }
        this.output = sjOut.toString();
        this.error = sjErr.toString();
        this.exitValue = process.exitValue();
        this.elapsed = _elapsed.elapsed_now();
    }

    private TS_OsProcess(CharSequence code, CodeType codeType) {
        TGS_FuncMTCUtils.run(() -> {
            var fileSuffix = codeType == CodeType.BAT ? "bat" : (codeType == CodeType.VBS ? "vbs" : null);
            if (fileSuffix == null) {
                TGS_FuncMTUUtils.thrw(TS_OsProcess.class.getSimpleName(),
                        "TS_Process(CharSequence code, CodeType codeType:" + codeType + ")",
                        "CodeType not recognized!");
            }
            var file = File.createTempFile("tmp" + TS_RandomUtils.nextString(5, true, true, false, false, null), "." + fileSuffix);
            file.delete();
            try (var fw = new FileWriter(file);) {
                fw.write(code.toString());
            }
            this.process = Runtime.getRuntime().exec(commandTokens);
            process();
            file.delete();
        }, e -> this.exception = e);
    }

    private TS_OsProcess(String[] commandTokens) {
        this(commandTokens, null, null);
    }

    private TS_OsProcess(String[] commandTokens, String[] envp, Path dir) {
        TGS_FuncMTCUtils.run(() -> {
            this.commandTokens = commandTokens;
            this.process = Runtime.getRuntime().exec(commandTokens, envp, dir == null ? null : dir.toFile());
            process();
        }, e -> this.exception = e);
    }

    private TS_OsProcess(List<String> commandTokens) {
        this(commandTokens.toArray(String[]::new));
    }

    private TS_OsProcess(CharSequence commandLine) {
        this(TS_ListCastUtils.toString(new StringTokenizer(commandLine.toString(), " ")));
    }

    public static TS_OsProcess of(CharSequence commandLine) {
        return new TS_OsProcess(commandLine);
    }

    public static TS_OsProcess of(String[] commandTokens, String[] envp, Path dir) {
        return new TS_OsProcess(commandTokens, envp, dir);
    }

    public static TS_OsProcess of(List<String> commandTokens) {
        return new TS_OsProcess(commandTokens);
    }

    public static TS_OsProcess of(String[] commandTokens) {
        return new TS_OsProcess(commandTokens);
    }

    public static TS_OsProcess of(CharSequence... commandTokens) {
        var strArr = new String[commandTokens.length];
        IntStream.range(0, commandTokens.length).forEach(i -> {
            strArr[i] = commandTokens[i].toString();
        });
        return new TS_OsProcess(strArr);
    }

    public static TS_OsProcess ofCode(CharSequence code, CodeType codeType) {
        return new TS_OsProcess(code, codeType);
    }

    public static TS_OsProcess ofPrg(CharSequence programCommand, CharSequence fileCommand) {
        var spc = " ";
        if (TS_OsPlatformUtils.isWindows()) {
            var t = "\"";
            return of(TGS_StringUtils.cmn().concat(t, programCommand, t, spc, t, fileCommand, t));
        }
        if (TS_OsPlatformUtils.isLinux()) {
            return of(TGS_StringUtils.cmn().concat(programCommand, spc, fileCommand));
        }
        return of(TGS_StringUtils.cmn().concat(programCommand, spc, fileCommand));
    }
}
