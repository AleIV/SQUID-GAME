package me.aleiv.core.paper.Games.glass;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.GlobalGame;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GlassListener implements Listener {

    Core instance;

    private int maniquiConChaleco = 84;
    private int maniquiSinChaleco = 42;

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
                this.instance.getGame().getGlobalGame().clothes(player, GlobalGame.Clothe.GLASS);
            }
        }
    }

}