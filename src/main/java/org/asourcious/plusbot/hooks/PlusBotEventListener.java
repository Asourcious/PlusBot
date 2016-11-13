package org.asourcious.plusbot.hooks;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.handle.AutoRoleHandler;
import org.asourcious.plusbot.handle.CommandHandler;

public class PlusBotEventListener extends ListenerAdapter {

    private AutoRoleHandler roleHandler;
    private CommandHandler commandHandler;

    public PlusBotEventListener(CommandHandler commandHandler, AutoRoleHandler roleHandler) {
        this.commandHandler = commandHandler;
        this.roleHandler = roleHandler;
    }

    @Override
    public void onReady(ReadyEvent event) {
        PlusBot.LOG.info(Constants.READY_MESSAGE);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        roleHandler.handleMemberJoin(event.getGuild(), event.getMember());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Statistics.numMessages++;
        if (!event.isFromType(ChannelType.PRIVATE) && !event.isFromType(ChannelType.TEXT))
            return;
        if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage())
            return;

        commandHandler.handle(event.getMessage(),
                event.getAuthor(),
                event.getChannel(),
                event.getGuild(),
                event.isFromType(ChannelType.PRIVATE));
    }
}
