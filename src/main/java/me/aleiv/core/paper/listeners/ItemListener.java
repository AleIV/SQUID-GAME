package me.aleiv.core.paper.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.aleiv.core.paper.Core;

public class ItemListener implements Listener {

    Core instance;

    List<Integer> helmetModel = List.of(4, 5, 6, 7, 24);

    public ItemListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onHelmet(PlayerInteractEvent e){
        var item = e.getItem();
        if(item != null && item.getItemMeta().hasCustomModelData() && helmetModel.contains(item.getItemMeta().getCustomModelData())){
            var player = e.getPlayer();
            var equip = player.getEquipment();
            if(equip.getHelmet() == null){
                equip.setHelmet(item);
                equip.setItemInMainHand(null);
            }
        }
    }
}
