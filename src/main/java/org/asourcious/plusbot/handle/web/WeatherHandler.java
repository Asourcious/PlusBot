package org.asourcious.plusbot.handle.web;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.config.Settings;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class WeatherHandler {

    private Settings settings;
    private Map<String, Pair<JSONObject,ZonedDateTime>> cache;

    public WeatherHandler(Settings settings) {
        this.settings = settings;
        this.cache = new HashMap<>();
    }

    public JSONObject getWeather(String query) {
        try {
            HttpResponse<String> response = Unirest.get("http://api.openweathermap.org/data/2.5/weather?q=" +
                    URLEncoder.encode(query, "UTF-8") + "&APPID=" + URLEncoder.encode(settings.getWeatherToken(), "UTF-8"))
                    .asString();

            if (response.getStatus() != 200)
                return null;

            return new JSONObject(response.getBody());
        } catch (UnirestException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public void cleanCache() {
        cache.keySet().parallelStream()
                .filter(key -> ZonedDateTime.now().isAfter(cache.get(key).getValue().plusMinutes(10)))
                .forEach(key -> cache.remove(key));
    }
}
