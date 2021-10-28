package me.aleiv.core.paper.map.renderer;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public class CustomRender extends MapRenderer {
    private final String fileName;
    private @Getter MapCanvas canvas;

    public CustomRender(String fileName) {
        this.fileName = fileName;
    }

    private void drawImage(MapCanvas canvas) throws IOException {
        var img = ImageIO.read(
                new File(System.getProperty("user.dir") + File.separatorChar + "maps" + File.separatorChar + fileName));

        canvas.drawImage(0, 0, img);

    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        try {
            drawImage(canvas);
            this.canvas = canvas;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
