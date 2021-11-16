package us.jcedeno.cookie;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import us.jcedeno.cookie.commands.CookieCMD;
import us.jcedeno.cookie.listener.CookieCaseListener;
import us.jcedeno.cookie.objects.CookieMap;

/**
 * The CookieManager class is a singleton that manages the cookie map views for
 * the SquidGame Cookie Game. Yes yes.
 * 
 * @author jcedeno
 */
public class CookieManager {

    private @Getter static Core instance;
    private @Getter CookieCMD cookieCMD;
    private @Getter ConcurrentHashMap<UUID, CookieMap> cookieMaps;

    public CookieManager(Core plugin) {
        instance = plugin;
        this.cookieCMD = new CookieCMD(plugin, this);
        this.cookieMaps = new ConcurrentHashMap<>();

        Bukkit.getPluginManager().registerEvents(new CookieCaseListener(this), plugin);

    }

}
