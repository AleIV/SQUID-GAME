package me.aleiv.core.paper.commands;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.aleiv.core.paper.commands.skin.PlayerSkin;

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
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    /** Demo */
    public static void main(String[] args) {

        var id = UUID.fromString("476d3e03-e9a5-4f85-ad96-53e46db89d63");

        var optionalJson = getPlayerSkins(id);
        if (optionalJson.isPresent()) {
            // PRint out
            System.out.println(gson.toJson(optionalJson.get()));

        } else {
            System.out.println("No skins found");
            // Generate skins if not found
            var playerSkins = createPlayerSkins(id);
            // If present, print out, else say it is empty
            if (playerSkins.isPresent()) {
                System.out.println(gson.toJson(playerSkins.get()));
            } else {
                System.out.println("2:No skins found");
            }
        }
    }

    /**
     * A function that gets or creates a player's skin variants. This will return
     * signed skin object, so this method is the one we'd like to use to change
     * skins or create npcs.
     * 
     * @param uuid The player's uuid
     * @return A list of player skins
     */
    public CompletableFuture<List<PlayerSkin>> getElseComputeSkins(UUID uuid) {

        final var future = new CompletableFuture<List<PlayerSkin>>();

        EXECUTOR_SERVICE.submit(() -> {
            var optionalJson = getPlayerSkins(uuid);

            if (optionalJson.isPresent()) {
                future.complete(optionalJson.get());
            } else {
                // Generate skins if not found
                var playerSkins = createPlayerSkins(uuid);

                /**
                 * TODO Write a while loop to guarante that the future returns signed skins
                 * only. Add a limit for the amount of attempt it can take to avoid infinite
                 * loop.
                 */

                // If present, print out, else say it is empty
                if (playerSkins.isPresent()) {
                    System.out.println(gson.toJson(playerSkins.get()));
                } else {
                    System.out.println("2:No skins found");
                }
            }

        });

        return future;
    }

    /**
     * Returns a player's skin collection if the player has one.
     * 
     * @param uuid The player's uuid.
     * @return An optional containing a player skin's collection or empty if not
     *         present.
     */
    public static Optional<List<PlayerSkin>> getPlayerSkins(UUID uuid) {
        var request = HttpRequest.newBuilder(URI.create(GET_SKIN_URI + uuid.toString())).GET()
                .header("accept", "application/json").build();
        try {
            var response = client.send(request, BodyHandlers.ofString());
            // Return if the response is not null. Assuming a player has already created a
            // skin before.
            if (response.body() != null && !response.body().equalsIgnoreCase("null")) {

                var jsonArray = gson.fromJson(response.body(), JsonArray.class);

                if (jsonArray != null) {
                    /*
                     * Get the first element which must represent the json object containg a skin
                     * Array. This skin array is a SkinCollection.
                     */
                    final var element = jsonArray.get(0);
                    /** If not null and json object is parseable. */
                    if (element != null && element.isJsonObject()) {
                        return getFromJsonObject(element.getAsJsonObject());
                    }
                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        /** If exists exceptionally, return empty optional */
        return Optional.empty();
    }

    /**
     * Creates a new skin for a player.
     * 
     * @param uuid The player's uuid.
     * @return And Optional containing the player's skin collection or empty if not
     */
    public static Optional<List<PlayerSkin>> createPlayerSkins(UUID uuid) {
        /** Build the request */
        final var request = HttpRequest.newBuilder(URI.create(CREATE_SKIN_URI + uuid.toString()))
                .PUT(BodyPublishers.noBody()).header("accept", "application/json").build();
        try {
            /** Attempt to process it and return it as a List of Plaker Skins. */
            return getFromJsonObject(
                    gson.fromJson(client.send(request, BodyHandlers.ofString()).body(), JsonObject.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            /** If there's an exception just return it empty. */
            return Optional.empty();
        }
    }

    /**
     * Util function to transform a json object containing a skins array to a list
     * of player skins.
     * 
     * @param jsonObject The json object to transform.
     * @return The list of player skins.
     */
    private static Optional<List<PlayerSkin>> getFromJsonObject(JsonObject jsonObject) {
        var skinsElement = jsonObject.get("skins");

        if (skinsElement != null && skinsElement.isJsonArray()) {
            var skins = skinsElement.getAsJsonArray();

            return Optional.of(List.of(gson.fromJson(skins, PlayerSkin[].class)));
        }

        return Optional.empty();

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
