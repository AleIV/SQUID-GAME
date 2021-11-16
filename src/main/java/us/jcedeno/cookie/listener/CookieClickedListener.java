package us.jcedeno.cookie.listener;

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
                    e.getPlayer().sendMessage("Works");
                } else {
                    e.getPlayer().sendMessage("Error");
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
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (e.getBlockFace() != BlockFace.UP) {
            return;
        }
        var block = e.getClickedBlock().getRelative(BlockFace.UP);
        // send location to player
        e.getPlayer().sendMessage("X: " + block.getX() + " Y: " + block.getY() + " Z: " + block.getZ());

        var map = cookieManager.getFrameMap().get(block);
        if (map != null) {
            e.getPlayer().sendMessage("Works2");
        } else {
            e.getPlayer().sendMessage("Error2");
        }

        var interaction = e.getInteractionPoint();
        if (interaction != null) {
            // Send interaction to player on message
            e.getPlayer().sendMessage(
                    "x: " + interaction.getX() + " \ny: " + interaction.getY() + " \nz: " + interaction.getZ());
        }
    }

}
