package me.aleiv.core.paper.Games.chicken;

import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.events.GameTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChickenListener implements Listener {

    private final @NonNull Core instance;
    private final List<UUID> playersAlreadyPushedButton;

    public ChickenListener(Core instance) {
        this.instance = instance;

        this.playersAlreadyPushedButton = new ArrayList<>();
    }

    @EventHandler
    public void gameTickEvent(GameTickEvent e){
        var chicken = ChickenCMD.chickenMode;
        var str = "";
        
        switch (chicken) {
            case HARD:{
                str = Character.toString('\u0271') + "";
            }break;

            case EASY:{
                str = Character.toString('\u0273') + "";
            }break;

            case MEDIUM:{
                str = Character.toString('\u0272') + "";
            }break;

            case NORMAL:{
                str = Character.toString('\u0268') + "";
            }break;
        
            default:
                break;
        }
        
        final var f = str;
        Bukkit.getOnlinePlayers().forEach(player ->{
            var equip = player.getEquipment();
            var helmet = equip.getHelmet();
            var y = player.getLocation().getY();
            if(helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasCustomModelData() && helmet.getItemMeta().getCustomModelData() == 24){
                if(y <= 45 ){
                    instance.showTitle(player, Character.toString('\u0273') + "", "", 0, 60, 0);
    
                }else{
                    instance.showTitle(player, f, "", 0, 60, 0);
                }
                
            }
        });
        
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock != null && clickedBlock.getType() == Material.WARPED_BUTTON && !this.playersAlreadyPushedButton.contains(e.getPlayer().getUniqueId())) {
            this.playersAlreadyPushedButton.add(e.getPlayer().getUniqueId());
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (this.instance.getGame().isGuard(p)) {
                    p.sendMessage("§aEl jugador §f" + e.getPlayer().getName() + "§a a ha puslado el botón");
                }
            });
        }
    }

    public void resetButtonPushes() {
        this.playersAlreadyPushedButton.clear();
    }
    
}
