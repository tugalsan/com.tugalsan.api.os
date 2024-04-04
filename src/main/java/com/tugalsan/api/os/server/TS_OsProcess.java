package com.tugalsan.api.os.server;

import com.tugalsan.api.list.server.TS_ListCastUtils;
import com.tugalsan.api.random.server.TS_RandomUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.client.TGS_UnionUtils;
import com.tugalsan.api.union.server.TS_UnionUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class TS_OsProcess {

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
    public Duration elapsed;

    public boolean exitValueOk() {
        return exitValue == 0;
    }

    public static enum CodeType {
        BAT,
        VBS
    }

    private void process() {
        var _elapsed = TS_TimeElapsed.of();
        try {
            this.pid = process.pid();
            process.waitFor();
            try (var is = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = is.lines().collect(Collectors.joining("\n"));
            }
            try (var es = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                error = es.lines().collect(Collectors.joining("\n"));
            }
            this.exitValue = process.exitValue();
        } catch (IOException ex) {
            this.exception = ex;
            return;
        } catch (InterruptedException ex) {
            TS_UnionUtils.throwAsRuntimeException(ex);
        }
        this.elapsed = _elapsed.elapsed_now();
    }

    private TS_OsProcess(CharSequence code, CodeType codeType) {
        try {
            var fileSuffix = codeType == CodeType.BAT ? "bat" : (codeType == CodeType.VBS ? "vbs" : null);
            if (fileSuffix == null) {
                TGS_UnionUtils.throwAsRuntimeException(
                        TS_OsProcess.class.getSimpleName(),
                        "TS_Process(CharSequence code, CodeType codeType:" + codeType + ")",
                        "CodeType not recognized!"
                );
            }
            var file = File.createTempFile("tmp" + TS_RandomUtils.nextString(5, true, true, false, false, null), "." + fileSuffix);
            file.delete();
            try (var fw = new FileWriter(file);) {
                fw.write(code.toString());
            }
            this.process = Runtime.getRuntime().exec(commandTokens);
            process();
            file.delete();
        } catch (IOException ex) {
            this.exception = ex;
        }
    }

    private TS_OsProcess(String[] commandTokens) {
        try {
            this.commandTokens = commandTokens;
            this.process = Runtime.getRuntime().exec(commandTokens);
            process();
        } catch (IOException ex) {
            this.exception = ex;
        }
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

    public static TS_OsProcess of(List<String> commandTokens) {
        return new TS_OsProcess(commandTokens);
    }

    public static TS_OsProcess of(String[] commandTokens) {
        return new TS_OsProcess(commandTokens);
    }

    public static TS_OsProcess ofCode(CharSequence code, CodeType codeType)  {
        return new TS_OsProcess(code, codeType);
    }

    public static TS_OsProcess ofPrg(CharSequence programCommand, CharSequence fileCommand) {
        var spc = " ";
        if (TS_OsPlatformUtils.isWindows()) {
            var t = "\"";
            return of(TGS_StringUtils.concat(t, programCommand, t, spc, t, fileCommand, t));
        }
        if (TS_OsPlatformUtils.isLinux()) {
            return of(TGS_StringUtils.concat(programCommand, spc, fileCommand));
        }
        return of(TGS_StringUtils.concat(programCommand, spc, fileCommand));
    }
}
