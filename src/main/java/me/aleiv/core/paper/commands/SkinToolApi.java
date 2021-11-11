package me.aleiv.core.paper.commands;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * The driver to interact with the skin tool api.
 * 
 * @author jcedeno
 */
public class SkinToolApi {
    /** Skin Tool IPFS Rest endpoint. */
    private final static String SKIN_TOOL_URI = "http://45.32.172.208:42069";
    /**
     * The endpoint to check if a player has a skin already created. Get Request.
     */
    private final static String GET_SKIN_URI = SKIN_TOOL_URI + "/skin/get/";
    /** The endpoint to create a new skin. PUT Request. */
    private final static String CREATE_SKIN_URI = SKIN_TOOL_URI + "/skin/create/";
    /** A http client to make the queries and parse data. */
    private final static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    private final static Gson gson = new Gson();

    /**
     * Returns a player's skin collection if the player has one.
     * 
     * @param uuid The player's uuid.
     * @return The player's skin collection.
     */
    public static JsonObject getPlayerSkins(UUID uuid) {
        var uri = URI.create(GET_SKIN_URI + uuid.toString());
        var request = HttpRequest.newBuilder(uri).GET().header("accept", "application/json").build();
        try {
            var response = client.send(request, BodyHandlers.ofString());
            // Return if the response is not null. Assuming a player has already created a
            // skin before.
            if (response.body() != null && !response.body().equalsIgnoreCase("null")) {
                return gson.fromJson(response.body(), JsonArray.class).get(0).getAsJsonObject();
            }
            return null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a new skin for a player.
     * 
     * @param uuid The player's uuid.
     * @return The player's skin collection.
     */
    public static JsonObject createPlayerSkins(UUID uuid) {
        var uri = URI.create(CREATE_SKIN_URI + uuid.toString());
        var request = HttpRequest.newBuilder(uri).PUT(BodyPublishers.noBody()).header("accept", "application/json")
                .build();
        try {
            var response = client.send(request, BodyHandlers.ofString());
            // Return if the response is not null. Assuming a player has already created a
            // skin before.
            return response.body() == null ? null : gson.fromJson(response.body(), JsonObject.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UUID getUserProfile(String name) {
        // https://api.ashcon.app/mojang/v2/user/

        var uri = URI.create("https://api.ashcon.app/mojang/v2/user/" + name);
        var request = HttpRequest.newBuilder(uri).header("accept", "application/json").build();
        try {
            var response = client.send(request, BodyHandlers.ofString());
            var json = gson.fromJson(response.body(), JsonObject.class);
            return UUID.fromString(json.get("uuid").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
