package me.aleiv.core.paper.Games.potato;

import lombok.Data;
import me.aleiv.core.paper.Core;

@Data
public class PotatoGame {
    Core instance;
    
    public PotatoGame(Core instance){
        this.instance = instance;

    }

}
