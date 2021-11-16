package us.jcedeno.cookie.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import us.jcedeno.cookie.CookieManager;
import us.jcedeno.cookie.objects.CookieEnum;

/**
 * @author jcedeno
 */
@CommandAlias("cookie")
public class CookieCMD extends BaseCommand {

    private CookieManager cookieManager;

    public CookieCMD(Core instance, CookieManager cookieManager) {

        this.cookieManager = cookieManager;
        instance.getCommandManager().registerCommand(this);
        instance.getCommandManager().getCommandCompletions().registerStaticCompletion("cookies",
                CookieEnum.getAll().stream().map(m -> m.name()).toList());

    }

    @CommandCompletion("@cookies")
    @Subcommand("give")
    public void giveCookie(Player sender, String cookieType) {
        CookieEnum cookie = CookieEnum.valueOf(cookieType.toUpperCase());
        if (cookie == null) {
            sender.sendMessage("Invalid cookie type");
            return;
        }

        var cookieCase = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        var itemMeta = cookieCase.getItemMeta();
        itemMeta.setCustomModelData(cookie.getModelData());

        cookieCase.setItemMeta(itemMeta);

        sender.getInventory().addItem(cookieCase);

    }

    // Command that toggles edit or not
    @Subcommand("edit")
    public void toggleEdit(CommandSender sender, @Optional Boolean bol) {
        if (bol == null) {
            CookieManager.EDIT = !CookieManager.EDIT;
        } else {
            CookieManager.EDIT = bol;
        }

        sender.sendMessage("Cookie edit mode is now: " + CookieManager.EDIT);
    }

}
