package me.aleiv.core.paper.vectors.commands;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.core.paper.Core;

/**
 * A command design to play around with vectors in minecraft.
 * 
 * @author jcedeno
 */
@CommandAlias("vectors|vec")
public class VectorsCommand extends BaseCommand {

    @CommandCompletion("/command1 & /commandn+1 ")
    @CommandAlias("chain0x|c0x")
    @Subcommand("chain-cmd|ccmd")
    public void rotate(Player sender, String args) {
        // Split the commands to be executed by && and execute them
        String[] cmds = args.split("&");
        Bukkit.broadcastMessage("Hello world");
        for (String cmd : cmds) {
            var cleanedUpCommand = cmd.trim().substring(1);
            sender.sendMessage(Core.getMiniMessage()
                    .parse(String.format("<green>Executing command <white>%s ", cleanedUpCommand)));
            Bukkit.dispatchCommand(sender, cleanedUpCommand);

        }

    }

    private HashMap<UUID, Entry<Vector, Vector>> vectorMap = new HashMap<>();

    @Subcommand("line-vector")
    public void buildLineVector(Player player) {
        var id = player.getUniqueId();
        // Get the block the player is looking at
        var block = player.getTargetBlock(null, 5);
        if (block == null) {
            player.sendMessage(Core.getMiniMessage().parse("<red>Block can't be null"));
            return;
        }
        var vector = block.getLocation().toVector();
        if (!vectorMap.containsKey(id)) {
            // If not contain, add the vector
            vectorMap.put(id, Map.entry(vector, vector));
            player.sendMessage(
                    Core.getMiniMessage().parse("<green>Set the origin vector <white>" + getCoordinates(block)));
        } else {
            // If contained check if the both the entry and value are contained.
            var entry = vectorMap.get(id);
            // Check if targetVector is not present, if not set it
            if (entry.getValue() == entry.getKey()) {
                vectorMap.put(id, Map.entry(entry.getKey(), vector));
                // Send a message to the player
                player.sendMessage(
                        Core.getMiniMessage().parse("<green>Set the target vector <white>" + getCoordinates(block)));
            } else {
                // Destroy the previous entries
                vectorMap.remove(id);
                // Tell the player
                player.sendMessage(Core.getMiniMessage().parse("<red>Destroyed the previous vectors."));
            }
        }
    }

    @Subcommand("trace-vector")
    @CommandCompletion("<material>")
    public void traceVector(Player sender, String materialName) {
        var material = Material.getMaterial(materialName.toUpperCase());
        var duple = vectorMap.get(sender.getUniqueId());
        var origin = duple.getKey();
        var target = duple.getValue();
        // Send error message to player if either is null and specify which one is it
        if (origin == null || target == null) {
            if (origin == null) {
                sender.sendMessage(Core.getMiniMessage().parse("<red>Origin vector is null"));
            }
            if (target == null) {
                sender.sendMessage(Core.getMiniMessage().parse("<red>Target vector is null"));
            }
            return;
        }

        // Get all the blocks
        var blockA = toBlock(origin.toBlockVector(), sender.getWorld()).getLocation();
        var blockB = toBlock(target.toBlockVector(), sender.getWorld()).getLocation();

        // Get all blocks in between
        var blocks = getBlocksOnRayTrace(blockA, blockB, 0.5, 100);
        // Reverse the list
        Collections.reverse(blocks);

        for (Block block : blocks) {

            var shiftedBlock = blockB.add(block.getLocation().toVector()).getBlock();
            shiftedBlock.setType(material);
            
            // Broadcast the name of the block modified
            Bukkit.broadcast(Core.getMiniMessage()
                    .parse("<gold><click:run_command:test>'" + getCoordinates(shiftedBlock) + "' "));
        }

    }

    /**
     * Stringifies a block
     * 
     * @param block The block to be stringified
     * @return
     */
    static String getCoordinates(Block block) {
        return String.format("(%s, %s, %s)", block.getX(), block.getY(), block.getZ());
    }

    /**
     * A function to ray trace a vector and get the blocks that it collides with.
     * 
     * @param start         The start location
     * @param end           The end location
     * @param direction     The direction vector
     * @param scalar        The scalar of the vector
     * @param maxIterations The maximun amount of iters in the ray cast.
     * @return
     */
    private List<Block> getBlocksOnRayTrace(Location start, Location end, double scalar, int maxIterations) {
        // A list to keep track of the blocks later
        var blocks = new ArrayList<Block>();
        var world = start.getWorld();

        // the oVector is the origin vector
        var direction = start.toVector().subtract(end.toVector());
        // the end vector
        var endVector = end.toVector();

        // Calculate the distance between the two refrence points and peridocailly
        // advance my multiplying the vector by a scalar factor.
        var distance = abs(start.distance(end));

        // Iterate until distance is 0 or max iterations are reached.
        while (maxIterations-- > 0 && distance > 0) {
            // Transform the current vector into a block vector
            var blockVector = direction.toBlockVector();
            // Transform the blockVector into actual block
            var block = toBlock(blockVector, world);
            // Add block to list if not already present
            if (!blocks.contains(block))
                blocks.add(block);

            // Now advance the vector my multiplying it by the scalar factor
            direction = direction.multiply(scalar);
            // Recalculate the distance between the two vector points.
            distance = distanceVectors(direction, endVector);
        }

        return blocks;
    }

    /**
     * Get's the distance between two vectors (absolute distance)
     * 
     * @param u The first vector
     * @param v The second vector
     * @return The distance between the two vectors
     */
    static double distanceVectors(Vector u, Vector v) {
        return abs(u.distance(v));
    }

    /**
     * Transform a BlockVector into an actual block object
     * 
     * @param vectorBlock the vector to transform
     * @param world       the world to get the block from
     * @return the block object
     */
    static Block toBlock(BlockVector vectorBlock, World world) {
        return world.getBlockAt(vectorBlock.getBlockX(), vectorBlock.getBlockY(), vectorBlock.getBlockZ());
    }

}
