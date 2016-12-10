package org.asourcious.plusbot;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.asourcious.plusbot.hooks.SimpleLogAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZonedDateTime;

public class BootLoader {

    private static PlusBot instance;
    private static final Logger LOG = LoggerFactory.getLogger(BootLoader.class);

    private BootLoader() {}

    public static void main(String[] args) throws IOException {
        SimpleLog.addListener(new SimpleLogAdapter());
        SimpleLog.LEVEL = SimpleLog.Level.OFF;

        createInstance();
        Statistics.startTime = ZonedDateTime.now();
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
            LOG.error("Error Creating instance", ex);
            System.exit(Constants.BOT_INITIALIZATION_ERROR);
        }
    }
}
