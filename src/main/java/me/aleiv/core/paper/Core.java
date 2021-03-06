package me.aleiv.core.paper;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.Games.chair.ChairCMD;
import me.aleiv.core.paper.Games.chair.ChairListener;
import me.aleiv.core.paper.Games.chicken.ChickenCMD;
import me.aleiv.core.paper.Games.cookie.CookieCMD;
import me.aleiv.core.paper.Games.cookie.CookieGame;
import me.aleiv.core.paper.Games.doll.DollCMD;
import me.aleiv.core.paper.Games.glass.GlassCMD;
import me.aleiv.core.paper.Games.hideseek.HideSeekCMD;
import me.aleiv.core.paper.Games.phone.PhoneCMD;
import me.aleiv.core.paper.Games.potato.PotatoCMD;
import me.aleiv.core.paper.Games.potato.PotatoListener;
import me.aleiv.core.paper.Games.rope.RopeCMD;
import me.aleiv.core.paper.Games.rope.RopeListener;
import me.aleiv.core.paper.commands.*;
import me.aleiv.core.paper.core.WebServer;
import me.aleiv.core.paper.listeners.*;
import me.aleiv.core.paper.objects.Participant;
import me.aleiv.core.paper.utilities.JsonConfig;
import me.aleiv.core.paper.utilities.NegativeSpaces;
import me.aleiv.core.paper.utilities.ResourcePackManager;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import me.aleiv.modeltool.core.EntityModelManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.jcedeno.libs.rapidinv.RapidInvManager;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpigotPlugin
public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private @Getter Game game;
    private @Getter PaperCommandManager commandManager;
    private @Getter static MiniMessage miniMessage = MiniMessage.get();
    //private @Getter CollisionManager collisionManager;
    //private @Getter VectorsManager vectorManager;
    private @Getter ProtocolManager protocolManager;
    private @Getter EntityModelManager entityModelManager;
    private @Getter ResourcePackManager resourcePackManager;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
     //@Getter private HologramPlayer hologramPlayer;

    @Override
    public void onEnable() {

        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
        RapidInvManager.register(this);
        BukkitTCT.registerPlugin(this);
        NegativeSpaces.registerCodes();
        entityModelManager = new EntityModelManager(this);
        resourcePackManager = new ResourcePackManager(this);
        resourcePackManager.setResoucePackURL("https://download.mc-packs.net/pack/8094106cfa519ce1a1aecd50dfae9b8a961cf77d.zip");
        resourcePackManager.setResourcePackHash("8094106cfa519ce1a1aecd50dfae9b8a961cf77d");
        resourcePackManager.setBypassPerm("squidgame.rp.bypass");

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);

        
        pullSpecialJson();
        pullParticipantJson();
        startWebServer();

        // Hologram start
        //this.hologramPlayer = new HologramPlayer(this);
        //this.hologramPlayer.spawnHologram();
        //Bukkit.getScheduler().runTask(this, () -> this.hologramPlayer.spawnHologram());

        // LISTENERS

        registerListener(new GlobalListener(this));
        registerListener(new RopeListener(this));
        registerListener(new CanceledListener(this));
        registerListener(new HideListener(this));
        registerListener(new ChairListener(this));
        registerListener(new MechanicsListener(this));
        registerListener(new PotatoListener(this));
        registerListener(new ItemListener(this));
        registerListener(new CinematicListener(this));
        registerListener(new FrozeListener(this));
        registerListener(new ChatListener(this));
        //registerListener(new HologramListener(this));
        registerListener(game.getGlobalStage());

        // COMMANDS

        commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerStaticCompletion("cookieTypes", Arrays.stream(CookieGame.CookieType.values()).map(CookieGame.CookieType::name).collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerStaticCompletion("rubiusanimation", List.of("idle", "walk", "intro", "penguin", "twerk", "jump", "asereje", "spin", "floss", "fortnite", "worm", "backflip", "antigravity", "robot", "gangnam", "dab"));

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
        commandManager.registerCommand(new CinemaCMD(this));
        commandManager.registerCommand(new DaysCMD(this));
        commandManager.registerCommand(new EffectsCMD(this));
        commandManager.registerCommand(new GuardnpcCMD(this));
        commandManager.registerCommand(new CPSCommand(this));

        commandManager.registerCommand(new AliasCMD(this));
        commandManager.registerCommand(new WinnerCMD(this));

        



        // Register skin command

        // Start collision manager
        //this.collisionManager = new CollisionManager(this);
        // Start vectors manager
        //this.vectorManager = new VectorsManager(this);

        // Start effect manager
    }

    private void startWebServer() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> new WebServer(this, 70));
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