package us.jcedeno.cookie.objects;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

/**
 * 
 * Custom renderer used to quickly paint an image onto a minecraft map canvas.
 * 
 * @author jcedeno
 */
public class CustomRender extends MapRenderer {
    private final String fileName;
    private final File actualFile;
    private @Getter MapCanvas canvas;

    public CustomRender(String fileName) {
        this.fileName = fileName;
        this.actualFile = new File(fileName);
    }

    private void drawImage(String fileName, MapCanvas canvas) throws IOException {
        canvas.drawImage(0, 0, ImageIO.read(actualFile != null ? actualFile : new File(fileName)));
    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        try {
            drawImage(this.fileName, canvas);

            this.canvas = canvas;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Static constructor for the Custom Renderer class.
     * 
     * @param fileName The name of the image file to be drawn.
     * @return A new instance of the CustomRender class.
     */
    public static CustomRender fromFile(String fileLocation) {
        return new CustomRender(fileLocation);
    }

}
