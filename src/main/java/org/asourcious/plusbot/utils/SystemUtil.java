package org.asourcious.plusbot.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public final class SystemUtil {
    private SystemUtil() {}

    private static Runtime runtime = Runtime.getRuntime();
    private static OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static double getCPUUsage() {
        return systemMXBean.getProcessCpuLoad() * 100;
    }

    public static long getUsedMemory() {
        return (runtime.totalMemory() - runtime.freeMemory()) / 1048576;
    }

    public static long getTotalMemory() {
        return runtime.maxMemory() / 1048576;
    }
}
