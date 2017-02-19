package org.asourcious.plusbot.util;

import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.math3.util.Precision;

import java.lang.management.ManagementFactory;

public final class SystemUtils {
    private SystemUtils() {}

    private static Runtime runtime = Runtime.getRuntime();
    private static OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static double getCPUUsage() {
        return Precision.round(systemMXBean.getProcessCpuLoad() * 100, 2);
    }

    public static long getUsedMemory() {
        return (runtime.totalMemory() - runtime.freeMemory()) / 1048576;
    }

    public static long getTotalMemory() {
        return runtime.maxMemory() / 1048576;
    }
}
