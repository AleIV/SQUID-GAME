package me.aleiv.core.paper.objects;

import lombok.Data;
import me.aleiv.core.paper.Game.Role;

@Data   
public class Participant {
    
    static int number = 0;
    String uuid;
    Role role = Role.PLAYER;
    boolean dead = false;
    String name;

    public Participant(String uuid, String name){
        this.uuid = uuid;
        this.name = name;
    }

    public Participant(String uuid, Role role, boolean dead, String name){
        this.uuid = uuid;
        this.role = role;
        this.dead = dead;
        this.name = name;
    }

}
