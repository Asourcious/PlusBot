package org.asourcious.plusbot;

import net.dv8tion.jda.core.utils.SimpleLog;

import java.time.OffsetDateTime;

public class BootLoader {

    private static PlusBot instance;
    private static final SimpleLog LOG = SimpleLog.getLog("BootLoader");

    private BootLoader() {}

    public static void main(String[] args) {
        createInstance();
        Statistics.startTime = OffsetDateTime.now();
    }

    public static void restart() {
        LOG.info("Restarting...");
        instance.shutdown(false);
        createInstance();
    }

    public static void shutdown() {
        LOG.info("Shutting down...");
        instance.shutdown(true);
    }

    private static void createInstance() {
        LOG.info("Creating instance...");
        try {
            instance = new PlusBot();
        } catch (Exception ex) {
            LOG.log(ex);
            System.exit(1);
        }
    }
}
