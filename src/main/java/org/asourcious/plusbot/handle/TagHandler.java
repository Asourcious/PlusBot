package org.asourcious.plusbot.handle;

import me.jagrosh.jagtag.JagTag;
import me.jagrosh.jagtag.Method;
import me.jagrosh.jagtag.Parser;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.source.GuildTags;
import org.asourcious.plusbot.util.DiscordUtils;

import java.util.Random;

public class TagHandler {

    private PlusBot plusBot;
    private Random random;

    public TagHandler(PlusBot plusBot) {
        this.plusBot = plusBot;
        this.random = new Random();
    }

    public void handleMessage(Message message, User author, TextChannel channel, Guild guild) {
        if (plusBot.getSettings().getBlacklists().has(guild.getId(), author.getId()))
            return;

        GuildTags tags = plusBot.getSettings().getGuildTags();
        String stripped = DiscordUtils.getStripped(plusBot, message);

        if (tags.existsWithName(guild.getId(), stripped)) {
            String response = tags.getByName(guild.getId(), stripped);
            Parser parser = JagTag.newDefaultBuilder()
                    .addMethod(new Method("random", (env,in) -> in[random.nextInt(in.length)], new String[0]))
                    .build();

            channel.sendMessage(parser.parse(response)).queue();
        }
    }
}
