package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.DiscordUtil;
import org.asourcious.plusbot.utils.FormatUtil;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandHandler {

    private PlusBot plusBot;
    private ExecutorService executorService;

    private final Map<String, Command> commands;
    private final Map<String, String> aliases;
    private final Map<String, RateLimitHandler> rateLimitHandlers;

    public CommandHandler(PlusBot plusBot) {
        this.plusBot = plusBot;
        this.executorService = Executors.newCachedThreadPool();
        this.commands = new ConcurrentHashMap<>();
        this.aliases = new ConcurrentHashMap<>();
        this.rateLimitHandlers = new ConcurrentHashMap<>();
    }

    public void handle(Message message, User author, TextChannel channel, Guild guild) {
        String prefix = DiscordUtil.getPrefix(plusBot, message);

        if (prefix == null)
            return;

        String formattedMessage = message.getRawContent()
                .substring(prefix.length())
                .replaceAll("<(@(!|&)?|#)\\d+>", "")
                .trim();
        String name = formattedMessage.split("\\s+")[0];
        String stripped = formattedMessage.substring(name.length()).trim();

        if (!hasCommand(name))
            return;
        Command command = getCommand(name);

        if (!PermissionLevel.hasPermission(guild.getMember(author), command.getRequiredPermission())) {
            channel.sendMessage(Constants.NOT_ENOUGH_PERMISSIONS).queue();
            return;
        }

        if (plusBot.getSettings().getConfiguration(guild).getBlacklist().contains(author.getId()))
            return;

        String response = command.isValid(message, stripped);
        if (response != null) {
            channel.sendMessage(response).queue();
            return;
        }

        Statistics.numCommands++;
        executorService.execute(() -> {
            OffsetDateTime nextAvailable = getRateLimitHandler(name).execute(channel.getId());

            if (nextAvailable == null) {
                command.execute(stripped, message, author, channel, guild);
            } else {
                channel.sendMessage("You have used this command too frequently. Try again in "
                        + FormatUtil.getFormattedDuration(OffsetDateTime.now(), nextAvailable) + ".").queue();
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
        rateLimitHandlers.values().forEach(RateLimitHandler::shutdown);
    }

    public Command getCommand(String name) {
        if (aliases.containsKey(name.toLowerCase()))
            return commands.get(aliases.get(name.toLowerCase()));

        return commands.get(name.toLowerCase());
    }

    public boolean hasCommand(String name) {
        return getCommand(name) != null;
    }

    public RateLimitHandler getRateLimitHandler(String name) {
        if (aliases.containsKey(name.toLowerCase()))
            return rateLimitHandlers.get(aliases.get(name.toLowerCase()));

        return rateLimitHandlers.get(name.toLowerCase());
    }

    public List<Command> getRegisteredCommands() {
        return Collections.unmodifiableList(new ArrayList<>(commands.values()));
    }

    public void registerCommand(Command command) {
        String name = command.getName().toLowerCase();
        commands.put(name, command);
        rateLimitHandlers.put(name, new RateLimitHandler(command));

        for (String alias : command.getAliases()) {
            aliases.put(alias.toLowerCase(), name);
        }
    }
}
