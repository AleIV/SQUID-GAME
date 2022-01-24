package me.aleiv.core.paper.Games.glass;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

public class GlassListener implements Listener {

    Core instance;

    private int maniquiConChaleco = 84;
    private int maniquiSinChaleco = 42;

    private String color1 = "<#f901ae>";

    public GlassListener(Core instance) {
        this.instance = instance;
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
                e.setCancelled(true);
                AnimationTools.setStandModel(stand, Material.BRICK, maniquiSinChaleco);
                Player player = e.getPlayer();
                var game = instance.getGame();
                game.getGlobalGame().clothes(player, GlobalGame.Clothe.GLASS);
                var participants = game.getParticipants();
                var participant = participants.get(player.getUniqueId().toString());
                var c =  MiniMessage.get().parse(color1 + stand.getName() + ". " + ChatColor.WHITE + "Jugador " + color1 + "#" + participant.getNumber() + " " + ChatColor.WHITE + participant.getName());
                Bukkit.broadcast(c);
               
            }
        }
    }

}