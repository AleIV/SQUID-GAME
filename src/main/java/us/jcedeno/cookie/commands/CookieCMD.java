package us.jcedeno.cookie.commands;

import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;
import us.jcedeno.cookie.objects.CookieEnum;
import us.jcedeno.cookie.objects.CookieMap;

/**
 * @author jcedeno
 */
@CommandAlias("cookie")
public class CookieCMD extends BaseCommand {
    private Core instance;

    public CookieCMD(Core instance) {
        this.instance = instance;

        this.instance.getCommandManager().registerCommand(this);
        this.instance.getCommandManager().getCommandCompletions().registerStaticCompletion("cookies",
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

        var item = cookieMap.getMapAsItem();

        sender.getInventory().addItem(item);

    }

}
