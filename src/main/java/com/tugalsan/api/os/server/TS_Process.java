package com.tugalsan.api.os.server;

import com.tugalsan.api.list.client.TGS_ListCastUtils;
import com.tugalsan.api.random.server.TS_RandomUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class TS_Process {

    public static Path executionPath() {
        return Path.of(System.getProperty("user.dir"));
    }

    public static String constructJarExecuterString(CharSequence file, List<String> args) {
        var sb = new StringBuilder()
                .append("\"").append(TS_JavaUtils.getPathJava()).append("\"")
                .append(" -jar ")
                .append("\"").append(file).append("\" ");
        args.stream().forEachOrdered(s -> sb.append(" ").append(s));
        return sb.toString();
    }

    public static String constructJarExtractorString(CharSequence file) {
        return TGS_StringUtils.concat("jar xf ", file);
    }

    public static String constructJarFileListString(CharSequence file) {
        return TGS_StringUtils.concat("jar tf ", file);
    }
    public String[] commandTokens;
    public Process process;
    public long pid;
    public String output;
    public String error;
    public Exception exception;
    public int exitValue;

    public boolean exitValueOk() {
        return exitValue == 0;
    }

    public static enum CodeType {
        BAT,
        VBS
    }

    private void process() {
        TGS_UnSafe.execute(() -> {
            this.pid = process.pid();
            process.waitFor();
            try ( var is = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = is.lines().collect(Collectors.joining("\n"));
            }
            try ( var es = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                error = es.lines().collect(Collectors.joining("\n"));
            }
            this.exitValue = process.exitValue();
        }, exception -> this.exception = exception);
    }

    private TS_Process(CharSequence code, CodeType codeType) {
        TGS_UnSafe.execute(() -> {
            var fileSuffix = codeType == CodeType.BAT ? "bat" : (codeType == CodeType.VBS ? "vbs" : null);
            if (fileSuffix == null) {
                TGS_UnSafe.catchMeIfUCan(TS_Process.class.getSimpleName(),
                        "TS_Process(CharSequence code, CodeType codeType:" + codeType + ")",
                        "CodeType not recognized!");
            }
            var file = File.createTempFile("tmp" + TS_RandomUtils.nextString(5, true, true, false, false, null), "." + fileSuffix);
            file.delete();
            try ( var fw = new FileWriter(file);) {
                fw.write(code.toString());
            }
            this.process = Runtime.getRuntime().exec(commandTokens);
            process();
            file.delete();
        }, exception -> this.exception = exception);
    }

    private TS_Process(String[] commandTokens) {
        TGS_UnSafe.execute(() -> {
            this.commandTokens = commandTokens;
            this.process = Runtime.getRuntime().exec(commandTokens);
            process();
        }, exception -> this.exception = exception);
    }

    private TS_Process(List<String> commandTokens) {
        this(commandTokens.toArray(String[]::new));
    }

    private TS_Process(CharSequence commandLine) {
        this(TGS_ListCastUtils.toString(new StringTokenizer(commandLine.toString(), " ")));
    }

    public static TS_Process of(CharSequence commandLine) {
        return new TS_Process(commandLine);
    }

    public static TS_Process of(List<String> commandTokens) {
        return new TS_Process(commandTokens);
    }

    public static TS_Process of(String[] commandTokens) {
        return new TS_Process(commandTokens);
    }

    public static TS_Process ofCode(CharSequence code, CodeType codeType) {
        return new TS_Process(code, codeType);
    }

    public static TS_Process ofPrg(CharSequence programCommand, CharSequence fileCommand) {
        var spc = " ";
        if (TS_OSUtils.isWindows()) {
            var t = "\"";
            return of(TGS_StringUtils.concat(t, programCommand, t, spc, t, fileCommand, t));
        }
        if (TS_OSUtils.isLinux()) {
            return of(TGS_StringUtils.concat(programCommand, spc, fileCommand));
        }
        return of(TGS_StringUtils.concat(programCommand, spc, fileCommand));
    }
}
