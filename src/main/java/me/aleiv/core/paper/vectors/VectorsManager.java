package me.aleiv.core.paper.vectors;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.vectors.commands.VectorsCommand;

public class VectorsManager {

    public Core instance;
    private VectorsCommand vectorsCommand;

    public VectorsManager(Core instance) {
        this.instance = instance;
        this.vectorsCommand = new VectorsCommand();
        // register the commands
        this.instance.getCommandManager().registerCommand(this.vectorsCommand);
    }

}
