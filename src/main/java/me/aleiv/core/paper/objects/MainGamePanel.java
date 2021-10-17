package me.aleiv.core.paper.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;
import us.jcedeno.libs.rapidinv.ItemBuilder;
import us.jcedeno.libs.rapidinv.RapidInv;

public class MainGamePanel extends RapidInv{

    @Getter GreenLightPanel greenLightPanel;

    Core instance;
    
    public MainGamePanel(Core instance){
        super(6*9, "Main game panel");

        this.instance = instance;
        this.greenLightPanel = new GreenLightPanel(this, instance);

        var greenLight = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(21))
            .name(ChatColor.AQUA + "Red light-Green light").build();

        this.addItem(greenLight, action ->{
            var player = (Player) action.getWhoClicked();
            this.greenLightPanel.open(player);
        });

    }


    
}
