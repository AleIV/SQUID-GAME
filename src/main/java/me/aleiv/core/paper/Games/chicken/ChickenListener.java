package me.aleiv.core.paper.Games.chicken;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.events.GameTickEvent;

public class ChickenListener implements Listener {

    private final @NonNull Core instance;

    public ChickenListener(Core instance) {
        this.instance = instance;
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
            if(helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasCustomModelData() && helmet.getItemMeta().getCustomModelData() == 24){
                instance.showTitle(player, f, "", 0, 60, 0);
            }
        });
        
    }
    
}
