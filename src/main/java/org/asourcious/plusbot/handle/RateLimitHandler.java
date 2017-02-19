package org.asourcious.plusbot.handle;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.Command.RateLimit;
import org.asourcious.plusbot.util.ConversionUtils;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class RateLimitHandler {

    private RateLimit rateLimit;

    private Map<String, Pair<Integer, ZonedDateTime>> useMap;
    private ScheduledExecutorService timer;

    public RateLimitHandler(Command command) {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern(command.getName() + " Ratelimit-pool thread %d").build();

        this.rateLimit = command.getRateLimit();
        this.useMap = new ConcurrentHashMap<>();
        this.timer = Executors.newSingleThreadScheduledExecutor(threadFactory);
    }

    public ZonedDateTime execute(String id) {
        if (rateLimit != null) {
            if (!useMap.containsKey(id)) {
                useMap.put(id, new Pair<>(1, ZonedDateTime.now().plus(rateLimit.getAmountTime(), ConversionUtils.convert(rateLimit.getUnit()))));
                timer.schedule(() -> useMap.remove(id), rateLimit.getAmountTime(), rateLimit.getUnit());
            } else {
                useMap.put(id, new Pair<>(useMap.get(id).getFirst() + 1, useMap.get(id).getSecond()));
            }

            if (useMap.get(id).getFirst() > rateLimit.getNumUses())
                return useMap.get(id).getSecond();
        }

        return null;
    }

    public void shutdown() {
        timer.shutdown();
    }
}