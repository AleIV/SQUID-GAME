package us.jcedeno.cookie.objects;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Enum designed to hold the possible cookie types for the CookieGame.
 * CookieEnums also hold the location of the image they will load.
 * 
 * @author jcedeno
 */
public enum CookieEnum {
    CREEPER(0), EYE(1), RODOLFO(2), SQUID(3);

    final String assetLocation;
    final int modelData;

    /**
     * Constructor for CookieEnum. It simply assumes the image location to be under
     * the assets folder with the format of "cookie_{enumType}.png".
     */
    CookieEnum(int modelData) {
        this.modelData = modelData;
        assetLocation = System.getProperty("user.dir") + File.separatorChar + "assets" + File.separatorChar + "cookie_"
                + toString().toLowerCase() + ".png";
    }

    /**
     * @return Returns the location of the image for the CookieEnum.
     */
    public String getAssetLocation() {
        return assetLocation;
    }

    /**
     * Util function designed to ease command autocompletion.
     * 
     * @return Returns a list of all the CookieEnums.
     */
    public static List<CookieEnum> getAll() {
        return Arrays.asList(values());
    }

    /**
     * @return Returns the modelData for the CookieEnum.
     */
    public int getModelData() {
        return modelData;
    }

}
