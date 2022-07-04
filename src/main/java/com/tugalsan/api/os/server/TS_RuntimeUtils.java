package com.tugalsan.api.os.server;

import com.tugalsan.api.random.server.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.unsafe.client.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class TS_RuntimeUtils {

    public static Path executionPath() {
        return Path.of(System.getProperty("user.dir"));
    }

    public static String runConsole_readResult(String[] commandLines) {
        return TGS_UnSafe.compile(() -> {
            var className = TS_RuntimeUtils.class.getSimpleName();
            var funcName = "runConsole_readResult";
            var arrow = " -> ";
            System.out.println(String.join("", className, arrow, funcName, arrow, executionPath().toString()));
            Arrays.stream(commandLines).forEach(line -> {
                System.out.println(arrow + line);
            });
            var p = Runtime.getRuntime().exec(commandLines);
            try ( var input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                return input.lines().collect(Collectors.joining("\n"));
            }
        });
    }

    public static String runConsole_readResult(CharSequence commandLine) {
        return TGS_UnSafe.compile(() -> {
            var className = TS_RuntimeUtils.class.getSimpleName();
            var funcName = "runConsole_readResult";
            var arrow = " -> ";
            System.out.println(String.join("", className, arrow, funcName, arrow, executionPath().toString(), arrow, commandLine));
            var p = Runtime.getRuntime().exec(commandLine.toString());
            try ( var input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                return input.lines().collect(Collectors.joining("\n"));
            }
        });
    }

    public static String runConsoleBAT_readResult(CharSequence batCode) {
        return TGS_UnSafe.compile(() -> {
            var file = File.createTempFile("tmp" + TS_RandomUtils.nextString(5, true, true, false, false, null), ".bat");
            file.delete();
            try ( var fw = new FileWriter(file);) {
                fw.write(batCode.toString());
            }
            var p = Runtime.getRuntime().exec(file.getPath());
            try ( var input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                var result = new StringJoiner("\n");
                System.out.println("runConsoleBAT_readResult.cmd:\n");
                while ((line = input.readLine()) != null) {
                    System.out.println("-->" + line + "\n");
                    result.add(line);
                }
                file.delete();
                return result.toString();
            }
        });
    }

    public static String runConsoleVBS_readResult(CharSequence vbCode) {
        return TGS_UnSafe.compile(() -> {
            var file = File.createTempFile("tmp", ".vbs");
            file.delete();
            try ( var fw = new FileWriter(file);) {
                fw.write(vbCode.toString());
            }
            var p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            try ( var input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                var result = new StringJoiner("\n");
                System.out.println("runConsoleVBS_readResult.cmd:\n");
                while ((line = input.readLine()) != null) {
                    System.out.println("-->" + line + "\n");
                    result.add(line);
                }
                file.delete();
                return result.toString();
            }
        });
    }

    public static void execute(CharSequence programCommand, CharSequence fileCommand) {
        var b = " ";
        if (TS_OSUtils.isWindows()) {
            var c = "\"";
            execute(TGS_StringUtils.concat(c, programCommand, c, b, c, fileCommand, c));
        } else if (TS_OSUtils.isLinux()) {
            execute(TGS_StringUtils.concat(programCommand, b, fileCommand));
        }
        execute(TGS_StringUtils.concat(programCommand, b, fileCommand));
    }

    public static void execute(String commandLine) {
        TGS_UnSafe.execute(() -> Runtime.getRuntime().exec(commandLine));
    }

    public static String constructJarExecuterString(CharSequence file, List<String> args) {
        var sb = new StringBuilder();
        args.add(0, TGS_StringUtils.concat("\"", file, "\""));
        args.add(0, "-jar");
        args.stream().forEachOrdered(s -> sb.append(" ").append(s));
        var sreturn = "\"" + TS_JavaUtils.getPathJava().toString() + "\"" + sb.toString();
        return sreturn;
    }

    public static String constructJarExtractorString(CharSequence file) {
        return TGS_StringUtils.concat("jar xf ", file);
    }

    public static String constructJarFileListString(CharSequence file) {
        return TGS_StringUtils.concat("jar tf ", file);
    }
}
