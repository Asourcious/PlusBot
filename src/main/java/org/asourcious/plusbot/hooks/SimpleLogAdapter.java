package org.asourcious.plusbot.hooks;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLogAdapter implements SimpleLog.LogListener {

    @Override
    public void onLog(SimpleLog simpleLog, SimpleLog.Level logLevel, Object message) {
        Logger log = LoggerFactory.getLogger(simpleLog.name);
        switch (logLevel) {
            case TRACE:
                log.trace(message.toString());
                break;
            case DEBUG:
                log.debug(message.toString());
                break;
            case INFO:
                log.info(message.toString());
                break;
            case WARNING:
                log.warn(message.toString());
                break;
            case FATAL:
                log.error(message.toString());
                break;
        }
    }

    @Override
    public void onError(SimpleLog simpleLog, Throwable err) {
        LoggerFactory.getLogger(simpleLog.name).error("An exception occurred", err);
    }
}
