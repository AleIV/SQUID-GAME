package us.jcedeno.cookie.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import us.jcedeno.cookie.CookieManager;
import us.jcedeno.cookie.objects.CookieEnum;
import us.jcedeno.cookie.objects.CookieMap;

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

        var cookieMap = new CookieMap(sender.getWorld(), cookie);
        // Put to map
        cookieManager.getCookieMaps().put(sender.getUniqueId(), cookieMap);

        var item = cookieMap.getCookieCase();

        sender.getInventory().addItem(item);

        // Send message
        sender.sendMessage("You have been given a " + cookie.name() + " cookie!");

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
