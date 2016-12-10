package org.asourcious.plusbot.commands.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class YodaSpeak extends Command {
    public YodaSpeak(PlusBot plusBot) {
        super(plusBot);
        this.help = "Converts a sentence to YodaSpeak";
        this.rateLimit = new RateLimit(1, 10, TimeUnit.SECONDS);
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        try {
            HttpResponse<String> response = Unirest.get("https://yoda.p.mashape.com/yoda?sentence=" + URLEncoder.encode(stripped, "UTF-8"))
                    .header("X-Mashape-Key", settings.getMashapeToken())
                    .header("Accept", "text/plain")
                    .asString();

            if (response.getStatus() != 200) {
                channel.sendMessage("Error contacting Yoda API!").queue();
                return;
            }

            channel.sendMessage(response.getBody()).queue();
        } catch (UnirestException | UnsupportedEncodingException ex) {
            PlusBot.LOG.error("An exception occurred", ex);
        }
    }
}
