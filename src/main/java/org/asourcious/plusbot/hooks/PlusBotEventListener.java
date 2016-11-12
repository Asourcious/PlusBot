package org.asourcious.plusbot.hooks;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.asourcious.plusbot.PlusBot;

public class PlusBotEventListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        PlusBot.LOG.info("PlusBot now online.");
    }
}
