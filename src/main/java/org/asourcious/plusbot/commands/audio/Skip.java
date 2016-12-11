package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.handle.audio.Player;

public class Skip extends NoArgumentCommand {

    public Skip(PlusBot plusBot) {
        super(plusBot);
        this.help = "Votes to skip the current song";
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);
        if (player.hasVoteSkipped(author.getId())) {
            channel.sendMessage("You have already voted to skip!").queue();
            return;
        }

        player.registerVoteSkip(author.getId());

        int numRequired = (int) Math.ceil(guild.getAudioManager().getConnectedChannel().getMembers().stream()
                .filter(member -> !member.getUser().isBot() && !member.getVoiceState().isGuildDeafened())
                .count() * .4D);

        channel.sendMessage(author.getAsMention() + ", your vote to skip has been acknowledged. "
                + player.getNumberOfVoteSkips() + "/" + numRequired + " votes to skip.").queue();

        if (player.getNumberOfVoteSkips() >= numRequired) {
            channel.sendMessage("Skipping **" + player.getPlayingTrack().getInfo().title + "**").queue();
            player.skip();
        }
    }
}
