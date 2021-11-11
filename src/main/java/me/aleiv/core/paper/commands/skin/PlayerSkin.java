package me.aleiv.core.paper.commands.skin;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A data class that represents a player skin.
 * 
 * @author jcedeno
 */
@AllArgsConstructor(staticName = "of")
@Data
public class PlayerSkin {
    String name;
    String value;
    String signature;

}
