package org.asourcious.plusbot.commands.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class UrbanDictionary extends Command {
    public UrbanDictionary(PlusBot plusBot) {
        super(plusBot);
        this.help = "Returns the definition of a word or phrase on UrbanDictionary";
        this.aliases = new String[] { "UD" };
        this.rateLimit = new RateLimit(1, 10, TimeUnit.SECONDS);
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        try {
            HttpResponse<String> response = Unirest.get("https://mashape-community-urban-dictionary.p.mashape.com/define?term=" + URLEncoder.encode(stripped, "UTF-8"))
                    .header("X-Mashape-Key", settings.getMashapeToken())
                    .header("Accept", "text/plain")
                    .asString();

            if (response.getStatus() != 200) {
                channel.sendMessage("Error contacting UrbanDictionary!").queue();
                return;
            }

            JSONObject json = new JSONObject(response.getBody());
            JSONObject word = json.getJSONArray("list").getJSONObject(0);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.addField("Word", word.getString("word"), false);
            embedBuilder.addField("Definition", word.getString("definition"), false);

            channel.sendMessage(embedBuilder.build()).queue();
        } catch (UnirestException | UnsupportedEncodingException | JSONException ex) {
            PlusBot.LOG.log(ex);
        }
    }
}
