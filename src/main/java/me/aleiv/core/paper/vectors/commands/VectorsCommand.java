package me.aleiv.core.paper.vectors.commands;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import me.aleiv.core.paper.npc.Npc;
import me.aleiv.core.paper.npc.utils.NPCOptions;

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

    private HashMap<UUID, LineVector> lineVectorMap = new HashMap<>();

    @Subcommand("line-vector")
    public void buildLineVector(Player player) {
        var id = player.getUniqueId();
        // Get the block the player is looking at
        var block = player.getTargetBlock(null, 5);
        if (block == null) {
            player.sendMessage(Core.getMiniMessage().parse("<red>Block can't be null"));
            return;
        }
        var vector = block.getLocation().add(0.5, 0.5, 0.5).toVector();
        if (!lineVectorMap.containsKey(id)) {
            // If not contain, add the vector
            var vectorEntry = LineVector.of(vector, null);
            lineVectorMap.put(id, vectorEntry);
            player.sendMessage(
                    Core.getMiniMessage().parse("<green>Set the origin vector <white>" + getCoordinates(block)));
        } else {
            // If contained check if the both the entry and value are contained.
            var entry = lineVectorMap.get(id);
            // Check if targetVector is not present, if not set it
            if (entry.getV() == null) {
                entry.setV(vector);
                var retrieve = lineVectorMap.get(id);
                // Send a message to the player
                player.sendMessage(Core.getMiniMessage()
                        .parse("<green>Set the target vector <white>" + getCoordinates(block) + " duple " + retrieve));
            } else {
                // Destroy the previous entries
                lineVectorMap.remove(id);
                // Tell the player
                player.sendMessage(Core.getMiniMessage().parse("<red>Destroyed the previous vectors."));
            }
        }
    }

    @Subcommand("trace-vector")
    @CommandCompletion("<material>")
    public void traceVector(Player sender, String materialName) {
        var material = Material.getMaterial(materialName.toUpperCase());
        var lineVector = lineVectorMap.get(sender.getUniqueId());
        var origin = lineVector.getU();
        var target = lineVector.getV();
        sender.sendMessage(Core.getMiniMessage().parse("<green>Going from vector U -> V. " + origin + " -> " + target));

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

        var vectors = getVectorsBetweenPoints(origin, target);
        vectors.forEach(all -> {
            new ParticleBuilder(Particle.BARRIER).location(all.toLocation(sender.getWorld())).receivers(20).force(true)
                    .count(10).spawn();
            Bukkit.getScheduler().runTaskLater(Core.getInstance(),
                    () -> all.toLocation(sender.getWorld()).getBlock().setType(material), 40);
        });

    }

    /**
     * Get all points from u to v.
     * 
     * @param u The first point.
     * @param v The second point.
     * @return The points from u to v.
     */
    List<Vector> getVectorsBetweenPoints(Vector u, Vector v) {
        // Substract u from v
        var uv = v.clone().subtract(u);
        // Normalize uv
        uv.normalize();
        // Get distance between u and v
        var distance = u.distance(v);

        // Create a vector array of the length of the distance between the two points
        var points = new Vector[(int) ceil(distance)];

        // Get all points from u to v
        for (int i = 1; i < points.length; i++) {
            points[i] = u.clone().add(uv.clone().multiply(i));
        }

        return Arrays.asList(points);
    }

    @Subcommand("spawn-npc")
    public void spawnNpc(Player player) {
        var npc = new Npc(Core.getInstance(), NPCOptions.builder().name("ElRichMC").location(player.getLocation())
                .signature(
                        "pHEY4reRxh/LdpC+OOnLMB5dvLjPFbGig6aHyn2yD3//KzK1gT9Euaw0kQgOLoDjehj+A/wRMM8fpxj8o9RKRswt5bdxCmyDeSsZrMVw0Iz7Vx0Ty3EwJhFWmtj6O6BYSkNWWR3cg4+Bd+W8LZvsORGD3DR3N4PCw5l+X4OSKCpqr0sD2kX6GOxruHgEwCBbrA98jgEoiHmBy9eUz2fCewA2SzPB6JBbHFcaaQ0ftlNzb/S3zMnkf/MK9iFb6D8ROUfoI9hVts9GQfYiKmEVcZy2dc2aKONGYN94ByF8ecDJYG+lQCCqctrnV/F54/+o1b7crgws9EJYBvwxAk8aPtGGF8k6eXsBzT39xD6qaiB4kRHAzScQ58KPUU7GNiD+tpqQWPiGWUp8xMUIGw+oooNOq+ZWGxnqUkgZuC2Anvo0NDBnzfMo0NSPdeUVljFgOE+aXcgWgEiezMQqWjDSYUx9U5Ltag1oAmo/RCg7anTonqkli0ZXrgROe+U3CCqQXQHyP1o90Y8eajlnlpwE1niF0nY6RIFwswd5YWm9iQc/ECXYOLv1tt5psq7mxqiIwkp8ah+VcZvUeisi4F51+XMg/6Jgi79UuuqN4svhoxvvxsmo13MmkHcssNx9GQrkpXnAemLH5R+OjOjxtV9E14rSzJA1+WX5Xn8qNyF2vA0=")
                .texture(
                        "ewogICJ0aW1lc3RhbXAiIDogMTYzMzY0MDUyMDM5MSwKICAicHJvZmlsZUlkIiA6ICIzNDkxZjJiOTdjMDE0MWE2OTM2YjFjMjJhMmEwMGZiNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJKZXNzc3N1aGgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk2ODU5NTY2N2IxYzMxZGI3YTY0N2Q5NzQwOTMwMTg2ZWRiMzM4NmRjNTcyZDYxNjE0MTBhNzM4Y2Q1OWE4OSIKICAgIH0KICB9Cn0=")
                .build());

        npc.showTo(player);

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
