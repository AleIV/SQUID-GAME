package me.aleiv.core.paper.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.mineskin.MineskinClient;
import org.mineskin.data.Skin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import me.aleiv.core.paper.Core;

/**
 * <p>
 * Class that represent a bukkit command used to change and visualize custom
 * skins. Intended to work with <a href=
 * "https://github.com/InfinityZ25/skin-tool-python">skin-tool-python</a> on the
 * backend.
 * </p>
 * 
 * @author jcedeno
 */
@CommandAlias("changeskin")
public class ChangeSkin extends BaseCommand {
    // Constants
    private static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private @NonNull Core instance;

    public ChangeSkin(Core instance) {
        this.instance = instance;
    }

    /**
     * A data object designed to hold the response from skin-tool-python to easily
     * interpret it.
     */
    @Data
    @AllArgsConstructor(staticName = "of")
    public static class SkinVariants {
        boolean isSlim;
        Map<String, String> variants;

        static SkinVariants from(String object) {

            var json = gson.fromJson(object, JsonObject.class);
            var data = json.get("data").getAsJsonObject();
            var tux = data.get("tux").getAsString();
            var part = data.get("participant").getAsString();
            var guard = data.get("guard").getAsString();
            var civ = data.get("civilian").getAsString();
            return SkinVariants.of(json.get("slim").getAsBoolean(),
                    Map.of("tux", tux, "part", part, "guard", guard, "civ", civ));
        }

    }

    static MineskinClient mineskinClient = new MineskinClient("skintooljava",
            "c674bd1cd9eff5816618afdac4da6ededd8c7c8fdd5f65888608cc73cdd770e3");

    public static void generateSkinsForName(String name) {
        final var start = System.currentTimeMillis();

        var uri = URI.create("http://localhost:8080/" + name);
        var request = HttpRequest.newBuilder(uri).header("accept", "application/json").build();

        var future = client.sendAsync(request, BodyHandlers.ofString()).whenComplete((result, exception) -> {

            try {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    // Get the result
                    var response = result.body();
                    var skinObject = SkinVariants.from(response);
                    // uploadSkin(skinObject.getTuxedo());
                    var iter = skinObject.getVariants().entrySet().iterator();
                    var list = new ArrayList<Skin>();
                    int n = 0;
                    while (iter.hasNext()) {
                        System.out.println(++n + " skin...");
                        var entry = iter.next();
                        var skin = entry.getValue();
                        Skin obj = null;
                        while (obj == null) {
                            System.out.println("Trying again...");
                            try {

                                obj = uploadSkin(skin);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        list.add(obj);
                    }
                    for (int i = 0; i < 10; i++) {
                        System.out.println("\n");
                    }
                    System.out.println("It took " + ((System.currentTimeMillis() - start) / 1000.0D)
                            + " seconds to complete the request of 4 skins.");

                    list.forEach(skins -> {
                        System.out.println(gson.toJson(skins));
                    });
                }

            } catch (Exception e) {
                // e.printStackTrace();
            }

        });
        while (!future.isDone()) {
        }
    }

    static Skin uploadSkin(String skinBase64) throws IOException {
        // Translate the skinBase64 to a file
        var skin = Base64.getDecoder().decode(skinBase64);
        var skinFile = new File("skin_" + UUID.randomUUID().toString().split("-")[0] + ".png");
        var skinFileOutputStream = new FileOutputStream(skinFile);
        skinFileOutputStream.write(skin);

        skinFileOutputStream.close();
        Skin skinObject = null;

        try {
            skinObject = mineskinClient.generateUpload(skinFile).get();
        } catch (Exception e) {
            // e.printStackTrace();
        }

        skinFile.delete();

        return skinObject;

    }

    public static void main(String[] args) {
        var names = List.of("TheWillyRex", "SrAuronPlay", "ElRubiusOMG", "DedReviil", "Grefunado");

        var start = System.currentTimeMillis();
        names.stream().forEach(ChangeSkin::generateSkinsForName);

        System.out.println("It took " + ((System.currentTimeMillis() - start) / 1000.0D)
                + " seconds to complete the request of " + names.size() + " skins.");
    }

}
