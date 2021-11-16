package us.jcedeno.cookie;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import us.jcedeno.cookie.commands.CookieCMD;

/**
 * The CookieManager class is a singleton that manages the cookie map views for
 * the SquidGame Cookie Game. Yes yes.
 * 
 * @author jcedeno
 */
public class CookieManager {

    private @Getter static Core instance;
    private @Getter CookieCMD cookieCMD;;

    private CookieManager(Core plugin) {
        instance = plugin;
        this.cookieCMD = new CookieCMD(plugin);

    }

}
