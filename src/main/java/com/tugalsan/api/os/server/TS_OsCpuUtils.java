package com.tugalsan.api.os.server;

import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.StringJoiner;

public class TS_OsCpuUtils {

    public static TGS_UnionExcuse<StringJoiner> getId() {
        return TGS_UnSafe.call(() -> {
            var sj = new StringJoiner("\n");
            var process = Runtime.getRuntime().exec("wmic cpu get ProcessorId");
            process.getOutputStream().close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().equals("")) {
                    sj.add(line.trim());
                }
            }
            return TGS_UnionExcuse.of(sj);
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    public static String getSerial() {
        return System.getenv("PROCESSOR_IDENTIFIER");
    }

    public static String getArchitecture() {
        return ManagementFactory.getOperatingSystemMXBean().getArch();
    }

    public static int getProcessorCount() {
        return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }

    public static long getLoad_currentThread() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    public static double getLoad_processorAverage_onErrorReturnMinusValue() {
        return ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
    }

}
