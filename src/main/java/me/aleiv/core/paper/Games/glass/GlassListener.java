package me.aleiv.core.paper.Games.glass;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.GlobalGame;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GlassListener implements Listener {

    private final Core instance;

    private int maniquiConChaleco = 84;
    private int maniquiSinChaleco = 42;

    private String color1 = "<#f901ae>";

    private List<UUID> playersThatGotJacket;
    private List<UUID> breakGlass;

    public GlassListener(Core instance) {
        this.instance = instance;

        this.playersThatGotJacket = new ArrayList<>();
        this.breakGlass = new ArrayList<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        var block = e.getClickedBlock();
        var player = e.getPlayer();
        var glass = instance.getGame().getGlassGame();
        if(block != null && player.getGameMode() == GameMode.CREATIVE && !player.isSneaking() && glass.isGlass(block.getType())){
            glass.breakGlass(block, true);
            
        }
    }

    @EventHandler
    public void onArmorStandInteraction(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            ArmorStand stand = (ArmorStand) e.getRightClicked();

            Integer id = AnimationTools.getStandModel(stand);
            if (id != null && id == maniquiConChaleco) {
                Player player = e.getPlayer();

                if (this.hasPlayerWithJacket(player.getUniqueId())) {
                    // Player already has the jacket
                    return;
                }

                e.setCancelled(true);
                AnimationTools.setStandModel(stand, Material.BRICK, maniquiSinChaleco);
                var game = instance.getGame();
                game.getGlobalGame().clothes(player, GlobalGame.Clothe.GLASS);
                var participants = game.getParticipants();
                var participant = participants.get(player.getUniqueId().toString());
                var c =  MiniMessage.get().parse(color1 + stand.getName() + ". " + ChatColor.WHITE + "Jugador " + color1 + "#" + participant.getNumber() + " " + ChatColor.WHITE + participant.getName());
                Bukkit.broadcast(c);
                this.addPlayerWithJacket(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!this.breakGlass.contains(e.getPlayer().getUniqueId())) return;

        this.glassBreakCheck(e.getTo());
    }

    public void addPlayerWithJacket(UUID uuid) {
        this.playersThatGotJacket.add(uuid);
    }

    public void removePlayerWithJacket(UUID uuid) {
        this.playersThatGotJacket.remove(uuid);
    }

    public boolean hasPlayerWithJacket(UUID uuid) {
        return this.playersThatGotJacket.contains(uuid);
    }

    public void addBreakGlass(UUID uuid) {
        this.breakGlass.add(uuid);
    }

    public void removeBreakGlass(UUID uuid) {
        this.breakGlass.remove(uuid);
    }

    public boolean hasBreakGlass(UUID uuid) {
        return this.breakGlass.contains(uuid);
    }

    public void glassBreakCheck(Location to) {
        to = to.clone();
        Block blockBelow = to.add(0, -0.2, 0).getBlock();
        if (blockBelow.getType() == Material.GLASS) {
            instance.getGame().getGlassGame().breakGlass(blockBelow, true);
        }
    }

}