package us.jcedeno.cookie.listener;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import us.jcedeno.cookie.CookieManager;

/**
 * 
 * @author jcedeno
 */
public class CookieCaseListener implements Listener {
    private final CookieManager cookieManager;

    public CookieCaseListener(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    private static Material CASE = Material.FERMENTED_SPIDER_EYE;

    @EventHandler
    public void onRightClickBlock(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }
        var id = e.getPlayer().getUniqueId();
        var map = cookieManager.getCookieMaps().get(id);
        if (map != null) {
            if (e.getItem() != null && e.getItem().getType() == CASE) {
                e.getItem().setType(Material.AIR);
                e.getPlayer().sendMessage("You have a cookie case!");
                var block = e.getClickedBlock();

                var itemFrame = (ItemFrame) block.getWorld().spawnEntity(
                        block.getLocation().getBlock().getRelative(BlockFace.UP).getLocation(), EntityType.ITEM_FRAME);
                itemFrame.setItem(map.getMapAsItem());
            }
        } else {
            e.getPlayer().sendMessage("You don't have a cookie case!");
        }

    }
}
