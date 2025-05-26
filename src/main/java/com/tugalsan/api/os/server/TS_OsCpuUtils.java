package com.tugalsan.api.os.server;

import com.sun.management.OperatingSystemMXBean;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.StringJoiner;
import javax.management.Attribute;
import javax.management.ObjectName;
import oshi.SystemInfo;

public class TS_OsCpuUtils {

    final private static String className = TS_OsCpuUtils.class.getSimpleName();

    public static TGS_UnionExcuse<String> getId() {
        return TGS_FuncMTCUtils.call(() -> {
            var process = Runtime.getRuntime().exec("wmic cpu get ProcessorId");
            process.getOutputStream().close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String lineCurrent;
            String lineValid = null;
            while ((lineCurrent = reader.readLine()) != null) {
                lineCurrent = lineCurrent.trim();
                if (!lineCurrent.equals("")) {
                    lineValid = lineCurrent;
                }
            }
            if (lineValid == null) {
                return TGS_UnionExcuse.ofExcuse(className, "getId", "lineValid == null");
            }
            return TGS_UnionExcuse.of(lineValid);
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

    @Deprecated //WHAT THE HELL IS THIS
    public static TGS_UnionExcuse<Long> getLoad_currentThread() {
        var value = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        if (value < 0) {
            return TGS_UnionExcuse.ofExcuse(className, "getLoad_currentThread", "val < 0");
        }
        return TGS_UnionExcuse.of(value);
    }

    @Deprecated //REUQIRES ADMIN
    public static TGS_UnionExcuse<Double> getLoad_processorAverage() {
        var value = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        if (value < 0) {
            return TGS_UnionExcuse.ofExcuse(className, "getLoad_processorAverage", "val < 0");
        }
        var percentage_with_1_decimal_point = ((int) (value * 1000) / 10.0);
        return TGS_UnionExcuse.of(percentage_with_1_decimal_point);
    }

    public static TGS_UnionExcuse<Integer> getLoad_percent_oshi(long delayMillis) {
        return TGS_FuncMTCUtils.call(() -> {
            var loads = new SystemInfo()
                    .getHardware()
                    .getProcessor()
                    .getProcessorCpuLoad(delayMillis);
            var val = (int) (Arrays.stream(loads).average().orElseThrow() * 100);
            return val > 0 ? TGS_UnionExcuse.of(val) : TGS_UnionExcuse.ofExcuse(className, "getLoad_percent_oshi", "val < 0");
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    @Deprecated //NOT WORKING
    public static TGS_UnionExcuse<Double> getLoad_process() {
        var value = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getProcessCpuLoad();
        if (value < 0) {
            return TGS_UnionExcuse.ofExcuse(className, "getLoad_process", "val < 0");
        }
        var percentage_with_1_decimal_point = ((int) (value * 1000) / 10.0);
        return TGS_UnionExcuse.of(percentage_with_1_decimal_point);
    }

    @Deprecated //NOT WORKING
    public static TGS_UnionExcuse<Double> getLoad_system() {
        var value = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCpuLoad();
        if (value < 0) {
            return TGS_UnionExcuse.ofExcuse(className, "getLoad_system", "val < 0");
        }
        var percentage_with_1_decimal_point = ((int) (value * 1000) / 10.0);
        return TGS_UnionExcuse.of(percentage_with_1_decimal_point);
    }

    @Deprecated //NOT WORKING
    public static TGS_UnionExcuse<Double> getLoad_jvm() {
        return TGS_FuncMTCUtils.call(() -> {
            var mbs = ManagementFactory.getPlatformMBeanServer();
            var name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            var list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});
            if (list.isEmpty()) {
                return TGS_UnionExcuse.ofExcuse(className, "getLoad_jvm", "list.isEmpty()");
            }
            var att = (Attribute) list.get(0);
            var value = (Double) att.getValue();
            if (value == -1.0) {
                return TGS_UnionExcuse.ofExcuse(className, "getLoad_jvm", "value == -1.0");
            }
            var percentage_with_1_decimal_point = ((int) (value * 1000) / 10.0);
            return TGS_UnionExcuse.of(percentage_with_1_decimal_point);
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    public static String toStringAll() {
        var sj = new StringJoiner("\n");
        sj.add("getArchitecture:" + TS_OsCpuUtils.getArchitecture());
        sj.add("getId:" + TS_OsCpuUtils.getId());
        sj.add("getLoad_currentThread:" + TS_OsCpuUtils.getLoad_currentThread());
        sj.add("getLoad_jvm:" + TS_OsCpuUtils.getLoad_jvm());
        sj.add("getLoad_process:" + TS_OsCpuUtils.getLoad_process());
        sj.add("getLoad_system:" + TS_OsCpuUtils.getLoad_system());
        sj.add("getProcessorCount:" + TS_OsCpuUtils.getProcessorCount());
        sj.add("getSerial:" + TS_OsCpuUtils.getSerial());
        sj.add("getLoad_processorAverage:" + TS_OsCpuUtils.getLoad_processorAverage());
        return sj.toString();
    }

}
