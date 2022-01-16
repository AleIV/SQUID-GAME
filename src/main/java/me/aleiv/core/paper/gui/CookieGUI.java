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

    private Filter filter;

    private final ChestGui gui;
    private final String titleTemplate;

    private PaginatedPane playersPane;

    public enum Filter {
        NONE(null, "TODOS"),
        WINNERS(true, "GANADORES"),
        NONWINNERS(false, "NO GANADORES");

        private Boolean filter;
        private String title;

        Filter(Boolean filter, String title) {
            this.filter = filter;
            this.title = title;
        }

        public Boolean getFilter(){
            return filter;
        }

        public String getTitle() {
            return title;
        }

        public Filter getNext(Filter filter){
            return values()[(filter.ordinal()+1)%values().length];
        }

    }

    public CookieGUI(Player player, Filter filter) {
        this.plugin = Core.getInstance();
        this.player = player;
        this.filter = filter;

        this.gui = new ChestGui(6, ".");
        this.titleTemplate = "&8(%p%/%m%) &6&lCookie Menu &8&l- &e&l%filter%";

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

        this.playersPane = getPlayerPages(this.filter);
        this.gui.addPane(this.playersPane);

        StaticPane navigation = this.getNavigation();
        this.gui.addPane(navigation);

        this.gui.addPane(getHopper());
    }

    private PaginatedPane getPlayerPages(Filter f) {
        PaginatedPane pane = new PaginatedPane(0, 0, 9, 5);

        List<CookieCapsule> ccs = this.plugin.getGame().getCookieGame().getAllCapules();
        ccs.sort((a, b) -> Integer.compare(b.getErrors(), a.getErrors()));
        pane.populateWithGuiItems(
                ccs.stream()
                        .filter(c ->
                                (f.getFilter() != null && f.getFilter() && c.isDone()) ||
                                (f.getFilter() != null && !f.getFilter() && !c.isDone()) ||
                                f.getFilter() == null
                        ).map(cc -> new GuiItem(this.getPlayerItem(cc), e -> {
                    e.setCancelled(true);
                    if (!cc.isDone()) {
                        cc.block();
                    }

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
            if (this.playersPane.getPage() < this.playersPane.getPages() - 1) {
                this.playersPane.setPage(this.playersPane.getPage() + 1);

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
            if (this.playersPane.getPage() > 0) {
                this.playersPane.setPage(this.playersPane.getPage() - 1);

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

    private StaticPane getHopper() {
        StaticPane button = new StaticPane(7, 5, 1, 1, Pane.Priority.HIGH);

        ItemStack hopper = new ItemStack(Material.HOPPER);
        ItemMeta meta = hopper.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lFILTER"));
        hopper.setItemMeta(meta);
        button.fillWith(hopper);

        button.setOnClick(e -> {
            e.setCancelled(true);
            this.filter = this.filter.getNext(this.filter);

            this.gui.getPanes().remove(this.playersPane);
            this.playersPane = this.getPlayerPages(this.filter);
            this.gui.addPane(this.playersPane);

            this.updateTitle();
            this.gui.update();
        });

        return button;
    }

    private void updateTitle() {
        this.gui.setTitle(
                ChatColor.translateAlternateColorCodes('&',
                        this.titleTemplate.replace("%p%", String.valueOf(this.playersPane.getPage()+1)).replace("%m%", String.valueOf(Math.max(this.playersPane.getPages(), 1))).replaceAll("%filter%", this.filter.getTitle())
                ));
    }

}
