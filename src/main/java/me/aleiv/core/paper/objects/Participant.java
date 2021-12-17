package me.aleiv.core.paper.objects;

import lombok.Data;
import me.aleiv.core.paper.Game.Role;

@Data   
public class Participant {
    
    String uuid;
    Role role = Role.PLAYER;
    boolean dead = false;

    public Participant(String uuid){
        this.uuid = uuid;
    }

    public Participant(String uuid, Role role, boolean dead){
        this.uuid = uuid;
        this.role = role;
        this.dead = dead;
    }

}
