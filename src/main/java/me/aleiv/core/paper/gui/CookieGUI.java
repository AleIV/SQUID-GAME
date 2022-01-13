package me.aleiv.core.paper.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.CookieCapsule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CookieGUI {

    private final Core plugin;
    private final Player player;

    private final ChestGui gui;
    private final String titleTemplate;

    private PaginatedPane camsPane;

    public CookieGUI(Player player) {
        this.plugin = Core.getInstance();
        this.player = player;

        this.gui = new ChestGui(6, ".");
        this.titleTemplate = "&8(%p%/%m%) &6&lCookie Menu";

        this.buildGui();
        this.updateTitle();

        this.gui.show(player);
    }

    private void buildGui() {
        this.gui.setOnTopDrag(e -> e.setCancelled(true));
        this.gui.setOnTopDrag(e -> e.setCancelled(true));

        ItemStack bgItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bgMeta = bgItem.getItemMeta();
        bgMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f "));
        bgMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        bgItem.setItemMeta(bgMeta);
        StaticPane bg = new StaticPane(0, 5, 9, 1, Pane.Priority.LOWEST);
        bg.fillWith(bgItem);
        bg.setOnClick(e -> e.setCancelled(true));
        this.gui.addPane(bg);

        this.camsPane = getPlayerPages();
        this.gui.addPane(this.camsPane);

        StaticPane navigation = this.getNavigation();
        this.gui.addPane(navigation);
    }

    private PaginatedPane getPlayerPages() {
        PaginatedPane pane = new PaginatedPane(0, 0, 9, 5);

        List<CookieCapsule> ccs = this.plugin.getGame().getCookieGame().getAllCapules();
        ccs.sort((a, b) -> Integer.compare(b.getErrors(), a.getErrors()));
        pane.populateWithGuiItems(
                ccs.stream().map(cc -> new GuiItem(this.getPlayerItem(cc), e -> {
                    e.setCancelled(true);
                    cc.block();

                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> this.player.teleport(cc.getPlayer().getLocation()), 5L);
                    player.closeInventory();
                })).collect(Collectors.toList())
        );

        return pane;
    }

    private StaticPane getNavigation() {
        StaticPane navigation = new StaticPane(0, 5, 9, 1, Pane.Priority.NORMAL);

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aNext Page"));
        nextPageMeta.setLore(Stream.of("&7Click to go to the next page").map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
        nextPage.setItemMeta(nextPageMeta);
        navigation.addItem(new GuiItem(nextPage, event -> {
            event.setCancelled(true);
            if (this.camsPane.getPage() < this.camsPane.getPages() - 1) {
                this.camsPane.setPage(this.camsPane.getPage() + 1);

                this.updateTitle();
                gui.update();
            }
        }), 8, 0);

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevPageMeta = prevPage.getItemMeta();
        prevPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aPrevious Page"));
        prevPageMeta.setLore(Stream.of("&7Click to go to the previous page").map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
        prevPage.setItemMeta(prevPageMeta);
        navigation.addItem(new GuiItem(prevPage, event -> {
            event.setCancelled(true);
            if (this.camsPane.getPage() > 0) {
                this.camsPane.setPage(this.camsPane.getPage() - 1);

                this.updateTitle();
                gui.update();
            }
        }), 0, 0);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClose"));
        closeMeta.setLore(Stream.of("&7Click to close this menu").map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
        close.setItemMeta(closeMeta);
        navigation.addItem(new GuiItem(close, event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        }), 4, 0);

        return navigation;
    }

    private ItemStack getPlayerItem(CookieCapsule cookieCapsule) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + cookieCapsule.getPlayer().getName());

        List<String> lore = List.of("&cFails: &f&l" + cookieCapsule.getErrors(), "&f ", "&7Click to block this player and tp");
        meta.setLore(lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        meta.setOwningPlayer(cookieCapsule.getPlayer());

        item.setItemMeta(meta);
        return item;
    }

    private void updateTitle() {
        this.gui.setTitle(
                ChatColor.translateAlternateColorCodes('&',
                        this.titleTemplate.replace("%p%", String.valueOf(this.camsPane.getPage()+1)).replace("%m%", String.valueOf(Math.max(this.camsPane.getPages(), 1)))
                ));
    }

}
