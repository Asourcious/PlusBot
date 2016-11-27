package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.source.GuildTags;
import org.asourcious.plusbot.utils.DiscordUtil;

public class TagHandler {

    private PlusBot plusBot;

    public TagHandler(PlusBot plusBot) {
        this.plusBot = plusBot;
    }

    public void handleMessage(Message message, User author, TextChannel channel, Guild guild) {
        if (plusBot.getSettings().getBlacklists().has(guild.getId(), author.getId()))
            return;

        GuildTags tags = plusBot.getSettings().getGuildTags();
        String stripped = DiscordUtil.getStripped(plusBot, message);

        if (tags.existsWithName(guild.getId(), stripped)) {
            String response = tags.getByName(guild.getId(), stripped);

            channel.sendMessage(response).queue();
        }
    }
}
