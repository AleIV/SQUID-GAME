package me.aleiv.core.paper;

import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.commands.AnimationStoreCMD;
import me.aleiv.core.paper.commands.GamesCMD;
import me.aleiv.core.paper.commands.SkinChanger;
import me.aleiv.core.paper.commands.SpecialCMD;
import me.aleiv.core.paper.commands.SquidCMD;
import me.aleiv.core.paper.detection.CollisionManager;
import me.aleiv.core.paper.listeners.GlobalListener;
import me.aleiv.core.paper.utilities.JsonConfig;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import me.aleiv.core.paper.vectors.VectorsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import us.jcedeno.libs.rapidinv.RapidInvManager;

@SpigotPlugin
public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private @Getter Game game;
    private @Getter PaperCommandManager commandManager;
    private @Getter AnimationStore animationStore;
    private @Getter static MiniMessage miniMessage = MiniMessage.get();
    private @Getter CollisionManager collisionManager;
    private @Getter VectorsManager vectorManager;
    private SkinChanger skinChanger;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable() {

        instance = this;

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);
        animationStore = new AnimationStore(this);

        RapidInvManager.register(this);
        BukkitTCT.registerPlugin(this);

        // LISTENERS

        Bukkit.getPluginManager().registerEvents(new GlobalListener(this), this);

        // COMMANDS

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SquidCMD(this));
        commandManager.registerCommand(new SpecialCMD(this));
        commandManager.registerCommand(new AnimationStoreCMD(this));
        commandManager.registerCommand(new GamesCMD(this));

        try {
            var jsonConfig = new JsonConfig("special.json");
            var list = jsonConfig.getJsonObject();
            var iter = list.entrySet().iterator();
            var map = AnimationTools.specialObjects;

            while (iter.hasNext()) {
                var entry = iter.next();
                var name = entry.getKey();
                var value = entry.getValue();
                map.put(name, value.getAsString());

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // Start collision manager
        this.collisionManager = new CollisionManager(this);
        // Start vectors manager
        this.vectorManager = new VectorsManager(this);

    }

    @Override
    public void onDisable() {

        var list = AnimationTools.specialObjects;

        try {
            var jsonConfig = new JsonConfig("special.json");
            var json = gson.toJson(list);
            var obj = gson.fromJson(json, JsonObject.class);
            jsonConfig.setJsonObject(obj);
            jsonConfig.save();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void adminMessage(String text) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("admin.perm"))
                player.sendMessage(text);
        });
    }

    public Component componentToString(String str) {
        return miniMessage.parse(str);
    }

    public void broadcastMessage(String text) {
        Bukkit.broadcast(miniMessage.parse(text));
    }

    public void sendActionBar(Player player, String text) {
        player.sendActionBar(miniMessage.parse(text));
    }

    public void showTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.showTitle(Title.title(miniMessage.parse(title), miniMessage.parse(subtitle), Times
                .of(Duration.ofMillis(50 * fadeIn), Duration.ofMillis(50 * stay), Duration.ofMillis(50 * fadeIn))));
    }

    public void sendHeader(Player player, String text) {
        player.sendPlayerListHeader(miniMessage.parse(text));
    }

    public void sendFooter(Player player, String text) {
        player.sendPlayerListFooter(miniMessage.parse(text));
    }

}