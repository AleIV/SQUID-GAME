package us.jcedeno.cookie.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import us.jcedeno.cookie.CookieManager;
import us.jcedeno.cookie.events.PlayerClickedCookieEvent;

/**
 * Listener for interactions with item frames.
 * 
 * @author jcedeno
 */
public class CookieClickedListener implements Listener {
    private CookieManager cookieManager;

    public CookieClickedListener(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    /**
     * Event called when a player click on an item frame which contains a full map.
     * Only the player who created the map (or a permission) should be able to
     * interact with it.
     */
    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        var entity = e.getRightClicked();

        if (entity != null && entity instanceof ItemFrame frame) {
            ItemStack itemInFrame = frame.getItem();
            if (itemInFrame != null && itemInFrame.getType() == Material.FILLED_MAP
                    && itemInFrame.getItemMeta() instanceof MapMeta map) {

                var entry = cookieManager.getFrameMap().get(entity.getLocation().getBlock());
                if (entry != null) {

                    var interactionPoint = entity.getLocation().add(e.getClickedPosition());

                    Bukkit.getPluginManager().callEvent(new PlayerClickedCookieEvent(interactionPoint, e.getPlayer(),
                            frame, !Bukkit.isPrimaryThread()));

                }
            }

        }
    }

    /**
     * Event called when a player clicks on a block which contains a map on a item
     * frame above of it.
     */
    @EventHandler
    public void onPlayerInteractAtItemFramesBlock(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getBlockFace() != BlockFace.UP) {
            return;
        }
        var block = e.getClickedBlock().getRelative(BlockFace.UP);
        // send location to player

        var map = cookieManager.getFrameMap().get(block);
        if (map != null) {
            var interaction = e.getInteractionPoint();
            if (interaction != null) {

                Bukkit.getPluginManager().callEvent(
                        new PlayerClickedCookieEvent(interaction, e.getPlayer(), map, !Bukkit.isPrimaryThread()));

            }
        }
    }

    @EventHandler
    public void onPlayerClickedCookie(PlayerClickedCookieEvent e) {
        var player = e.getPlayer();
        var frame = e.getItemFrame();
        var interaction = e.getInteractionPoint();

        player.sendMessage("You've clicked at interaction " + interaction.toString());
    }

}
