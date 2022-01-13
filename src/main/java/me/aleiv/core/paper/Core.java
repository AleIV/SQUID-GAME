package me.aleiv.core.paper;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.Games.chair.ChairCMD;
import me.aleiv.core.paper.Games.chair.ChairListener;
import me.aleiv.core.paper.Games.chicken.ChickenCMD;
import me.aleiv.core.paper.Games.cookie.CookieCMD;
import me.aleiv.core.paper.Games.cookie.CookieGame;
import me.aleiv.core.paper.Games.doll.DollCMD;
import me.aleiv.core.paper.Games.glass.GlassCMD;
import me.aleiv.core.paper.Games.glass.GlassListener;
import me.aleiv.core.paper.Games.hideseek.HideSeekCMD;
import me.aleiv.core.paper.Games.phone.PhoneCMD;
import me.aleiv.core.paper.Games.potato.PotatoCMD;
import me.aleiv.core.paper.Games.potato.PotatoListener;
import me.aleiv.core.paper.Games.rope.RopeCMD;
import me.aleiv.core.paper.Games.rope.RopeListener;
import me.aleiv.core.paper.commands.ClothesCMD;
import me.aleiv.core.paper.commands.ElevatorsCMD;
import me.aleiv.core.paper.commands.MainCMD;
import me.aleiv.core.paper.commands.PlayersCMD;
import me.aleiv.core.paper.commands.SpecialCMD;
import me.aleiv.core.paper.commands.SquidCMD;
import me.aleiv.core.paper.commands.TestCMD;
import me.aleiv.core.paper.commands.UtilsCMD;
import me.aleiv.core.paper.core.WebServer;
import me.aleiv.core.paper.detection.CollisionManager;
import me.aleiv.core.paper.listeners.CanceledListener;
import me.aleiv.core.paper.listeners.GlobalListener;
import me.aleiv.core.paper.listeners.HideListener;
import me.aleiv.core.paper.listeners.ItemListener;
import me.aleiv.core.paper.listeners.MechanicsListener;
import me.aleiv.core.paper.objects.Participant;
import me.aleiv.core.paper.utilities.JsonConfig;
import me.aleiv.core.paper.utilities.NegativeSpaces;
import me.aleiv.core.paper.utilities.ResourcePackManager;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import me.aleiv.core.paper.vectors.VectorsManager;
import me.aleiv.modeltool.core.EntityModelManager;
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
    private @Getter static MiniMessage miniMessage = MiniMessage.get();
    private @Getter CollisionManager collisionManager;
    private @Getter VectorsManager vectorManager;
    private @Getter ProtocolManager protocolManager;
    private @Getter EntityModelManager entityModelManager;
    private @Getter ResourcePackManager resourcePackManager;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable() {

        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
        RapidInvManager.register(this);
        BukkitTCT.registerPlugin(this);
        NegativeSpaces.registerCodes();
        entityModelManager = new EntityModelManager(this);
        resourcePackManager = new ResourcePackManager(this);
        resourcePackManager.setResoucePackURL("https://download.mc-packs.net/pack/502d587ba808f6145e9a04d9c755c8b9cf518ea9.zip");
        resourcePackManager.setResourcePackHash("502d587ba808f6145e9a04d9c755c8b9cf518ea9");

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);

        pullSpecialJson();
        pullParticipantJson();
        startWebServer();

        // LISTENERS

        registerListener(new GlobalListener(this));
        registerListener(new RopeListener(this));
        registerListener(new CanceledListener(this));
        registerListener(new HideListener(this));
        registerListener(new ChairListener(this));
        registerListener(new MechanicsListener(this));
        registerListener(new PotatoListener(this));
        registerListener(new GlassListener(this));
        registerListener(new ItemListener(this));

        // COMMANDS

        commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerStaticCompletion("cookieTypes", Arrays.stream(CookieGame.CookieType.values()).map(CookieGame.CookieType::name).collect(Collectors.toList()));

        commandManager.registerCommand(new DollCMD(this));
        commandManager.registerCommand(new MainCMD(this));
        commandManager.registerCommand(new SquidCMD(this));
        commandManager.registerCommand(new RopeCMD(this));
        commandManager.registerCommand(new HideSeekCMD(this));
        commandManager.registerCommand(new ElevatorsCMD(this));
        commandManager.registerCommand(new ChairCMD(this));
        commandManager.registerCommand(new PotatoCMD(this));
        commandManager.registerCommand(new GlassCMD(this));
        commandManager.registerCommand(new PhoneCMD(this));
        commandManager.registerCommand(new CookieCMD(this));
        commandManager.registerCommand(new ChickenCMD(this));

        commandManager.registerCommand(new UtilsCMD(this));
        commandManager.registerCommand(new SpecialCMD(this));
        commandManager.registerCommand(new TestCMD(this));
        commandManager.registerCommand(new PlayersCMD(this));
        commandManager.registerCommand(new ClothesCMD(this));


        // Register skin command

        // Start collision manager
        this.collisionManager = new CollisionManager(this);
        // Start vectors manager
        this.vectorManager = new VectorsManager(this);

        // Start effect manager

    }

    private void startWebServer() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> new WebServer(this, 80));
    }

    @Override
    public void onDisable() {


    }

    public void pullParticipantJson(){
        try {
            var jsonConfig = new JsonConfig("participants.json");
            var list = jsonConfig.getJsonObject();
            var iter = list.entrySet().iterator();
            var map = game.getParticipants();

            while (iter.hasNext()) {
                var entry = iter.next();
                var name = entry.getKey();
                var value = entry.getValue();
                var participant = gson.fromJson(value, Participant.class);
                map.put(name, participant);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void saveParticipantJson(){
        var list = game.getParticipants();

        try {
            var jsonConfig = new JsonConfig("participants.json");
            var json = gson.toJson(list);
            var obj = gson.fromJson(json, JsonObject.class);
            jsonConfig.setJsonObject(obj);
            jsonConfig.save();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void saveSpecialJson() {
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

    private void pullSpecialJson() {
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
    }

    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
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