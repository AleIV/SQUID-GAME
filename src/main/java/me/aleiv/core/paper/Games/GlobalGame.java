package me.aleiv.core.paper.Games;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.events.GameStartedEvent;
import me.aleiv.core.paper.utilities.Frames;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import us.jcedeno.libs.rapidinv.ItemBuilder;

public class GlobalGame {
    Core instance;

    public GlobalGame(Core instance) {
        this.instance = instance;
    }

    public enum Clothe {
        SMOKIN, UNIFORM, GLASS, CHICKEN, AURON, RUBIUS, KOMANCHE
    }

    public enum Mask {
        CREEPER, SQUID, EYE, BOSS, CHEF
    }

    public void clothes(Player player, Mask mask) {
        var equip = player.getEquipment();
        var inv = player.getInventory();
        inv.clear();

        switch (mask) {
            case CREEPER: {
                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(6)).name("Mask").build();
                equip.setHelmet(hat);

                var revolver = new ItemBuilder(Material.CROSSBOW).meta(meta -> meta.setCustomModelData(1))
                        .name("Revolver")
                        .build();
                inv.addItem(revolver);
                inv.addItem(new ItemStack(Material.ARROW, 64));
            }
                break;

            case SQUID: {
                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(4)).name("Mask").build();
                equip.setHelmet(hat);

                equip.setItemInOffHand(new ItemStack(Material.CROSSBOW));
                var revolver = new ItemBuilder(Material.CROSSBOW).meta(meta -> meta.setCustomModelData(1))
                        .name("Revolver")
                        .build();
                inv.addItem(revolver);
                inv.addItem(new ItemStack(Material.ARROW, 64));

                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.ARMOR", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var harness = new ItemBuilder(Material.IRON_CHESTPLATE)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                inv.addItem(harness);
            }
                break;

            case EYE: {
                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(5)).name("Mask").build();
                equip.setHelmet(hat);
            }
                break;

            case BOSS: {
                inv.clear();
                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(7)).name("Mask").build();
                equip.setHelmet(hat);

                var r = new ItemBuilder(Material.CROSSBOW).meta(meta -> meta.setCustomModelData(1))
                        .name("Revolver")
                        .build();
                inv.addItem(r);
                inv.addItem(new ItemStack(Material.ARROW, 64));
            }
                break;

            case CHEF: {
                inv.clear();
                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(7)).name("Mask").build();
                equip.setHelmet(hat);

                var r = new ItemBuilder(Material.CROSSBOW).meta(meta -> meta.setCustomModelData(1))
                        .name("Revolver")
                        .build();
                inv.addItem(r);
                inv.addItem(new ItemStack(Material.ARROW, 64));

                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.ARMOR", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chef = new ItemBuilder(Material.GOLDEN_CHESTPLATE)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setChestplate(chef);
            }
                break;

            default:
                break;
        }
    }

    public void clothes(Player player, Clothe clothe) {
        var equip = player.getEquipment();
        var inv = player.getInventory();
        inv.clear();

        switch (clothe) {
            case SMOKIN: {
                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.MOVEMENT.SPEED", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chest = new ItemBuilder(Material.LEATHER_CHESTPLATE).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, armor)).build();

                equip.setChestplate(chest);

                var legs = new ItemBuilder(Material.LEATHER_LEGGINGS).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setLeggings(legs);

            }
                break;

            case UNIFORM: {
                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.MOVEMENT.SPEED", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chest = new ItemBuilder(Material.DIAMOND_CHESTPLATE).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setChestplate(chest);

                var legs = new ItemBuilder(Material.DIAMOND_LEGGINGS).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setLeggings(legs);
            }
                break;

            case GLASS: {
                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.ARMOR", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chest = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setChestplate(chest);

                var legs = new ItemBuilder(Material.DIAMOND_LEGGINGS).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setLeggings(legs);
            }
                break;

            case CHICKEN: {
                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(24)).name("Bandage")
                        .build();
                equip.setHelmet(hat);

                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.ARMOR", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chest = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setChestplate(chest);

                var legs = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setLeggings(legs);
            }
                break;

            case AURON: {

                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(63)).name("Mask")
                        .build();
                equip.setHelmet(hat);

                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.ARMOR", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chest = new ItemBuilder(Material.NETHERITE_CHESTPLATE)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setChestplate(chest);

                var legs = new ItemBuilder(Material.NETHERITE_LEGGINGS).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setLeggings(legs);
            }
                break;

            case RUBIUS: {

                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(65)).name("Mask")
                        .build();
                equip.setHelmet(hat);

                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.ARMOR", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chest = new ItemBuilder(Material.NETHERITE_CHESTPLATE).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setChestplate(chest);

                var legs = new ItemBuilder(Material.NETHERITE_LEGGINGS).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setLeggings(legs);
            }
                break;

            case KOMANCHE: {

                var hat = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(64)).name("Mask")
                        .build();
                equip.setHelmet(hat);

                final AttributeModifier armor = new AttributeModifier(UUID.randomUUID(),
                        "GENERIC.ARMOR", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);

                var chest = new ItemBuilder(Material.NETHERITE_CHESTPLATE).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setChestplate(chest);

                var legs = new ItemBuilder(Material.NETHERITE_LEGGINGS).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .meta(meta -> meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor)).build();

                equip.setLeggings(legs);
            }
                break;

            default:
                break;
        }
    }

    public CompletableFuture<Boolean> applyGas(List<Player> players) {
        players.forEach(player -> {
            var loc = player.getLocation();
            player.playSound(loc, "squid:sfx.gas", 11, 1);
        });
        return playAnimation(players, 3402, 3601, 0, 0);
    }

    public CompletableFuture<Boolean> sendCountDown(List<Player> players) {
        players.forEach(player -> {
            var loc = player.getLocation();
            player.playSound(loc, "squid:sfx.countdown", 11, 1);
        });
        return playAnimation(players, 3602, 4392, 0, 0);
    }

    public void makeAllSleep() {
        Bukkit.getWorlds().forEach(it -> {
            it.setTime(20000);
        });
        var game = instance.getGame();
        var beds = AnimationTools.findLocations("BED");
        var guardBeds = AnimationTools.findLocations("GUARDB");
        var players = Bukkit.getOnlinePlayers();
        var participants = players.stream().filter(player -> game.isPlayer(player)).map(player -> (Player) player)
                .toList();
        var guards = players.stream().filter(player -> game.isGuard(player)).filter(p -> p.getGameMode() != GameMode.SPECTATOR).map(player -> (Player) player).toList();

        AnimationTools.forceSleep(participants, beds);
        AnimationTools.forceSleep(guards, guardBeds);
    }

    public void sleep(Player player, Location loc) {
        AnimationTools.forceSleep(player, loc);
    }

    public void playSquidGameStart() {
        var task = new BukkitTCT();

        for (int i = 0; i < 160; i++) {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        instance.showTitle(player, Character.toString('\u3400') + "", "", 0, 20, 0);
                    });
                }
            }, 50);
        }

        var tk = task.execute();
        tk.thenAccept(action -> {
            Bukkit.getScheduler().runTask(instance, t ->{
                Bukkit.getPluginManager().callEvent(new GameStartedEvent());
            });
        });

    }

    public CompletableFuture<Boolean> playAnimation(List<Player> players, Integer from, Integer until, int fadeIn,
            int fadeOut) {

        var task = new BukkitTCT();

        var animation = Frames.getFramesCharsIntegersAll(from, until);

        animation.forEach(frame -> {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    players.forEach(player -> {
                        instance.showTitle(player, frame + "", "", fadeIn, 20, fadeOut);
                    });
                }
            }, 50);
        });

        task.addWithDelay(new BukkitRunnable() {
            @Override
            public void run() {
                players.forEach(player -> {
                    instance.sendActionBar(player, Character.toString('\u3400') + "");
                });
            }
        }, 50);

        return task.execute();
    }

}
