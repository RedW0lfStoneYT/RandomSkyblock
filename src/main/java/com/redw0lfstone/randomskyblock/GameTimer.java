package com.redw0lfstone.randomskyblock;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GameTimer {

    private int count = 0;

    public GameTimer() {

        Random randomTimer = new Random();
        int timerLow = RandomSkyblock.getInstance().getConfig().getInt("timer.min");
        int timerHigh = RandomSkyblock.getInstance().getConfig().getInt("timer.max");
        Random blockRandom = new Random();
        new BukkitRunnable() {

            @Override
            public void run() {
                Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
                if (players.size() > 0) {

                    FileConfiguration config = RandomSkyblock.getInstance().getConfig();

                    for (Player player : players) {
                        World world = player.getWorld();
                        Material mat = randomEnum(Material.class, blockRandom);
                        for (int x = 0; x < 2; x++) {
                            if (!mat.isBlock()) mat = randomEnum(Material.class, blockRandom);
                        }
                        Location location = player.getLocation();
                        int radius = (getCount() < config.getInt("inital_random.times") ? config.getInt("inital_random.radius") : config.getInt("random.radius")) / 2;
                        int xMin = location.getBlockX() - radius;
                        int xMax = location.getBlockX() + radius;
                        int randX = blockRandom.nextInt(xMax - xMin) + xMin;

                        int yMin = location.getBlockY() - radius;
                        int yMax = location.getBlockY() + radius;
                        int randY = blockRandom.nextInt(yMax - yMin) + yMin;

                        int zMin = location.getBlockZ() - radius;
                        int zMax = location.getBlockZ() + radius;
                        int randZ = blockRandom.nextInt(zMax - zMin) + zMin;

                        if (randY < 0) randY = 0;
                        try {
                            Block block = world.getBlockAt(randX, randY, randZ);
                            if (config.getBoolean("only_replace_air") && !block.getType().isAir()) {
                                return;
                            }
                            block.setType(mat, false);
                            if (mat.equals(Material.CHEST) || mat.equals(Material.TRAPPED_CHEST)) {
                                mat = randomEnum(Material.class, blockRandom);
                                Chest chest = (Chest) block.getState();
                                ItemStack item = new ItemStack(mat);
                                item.setAmount(blockRandom.nextInt(config.getInt("random.item_max") - config.getInt("random.item_min")) + config.getInt("random.item_min"));
                                chest.getInventory().addItem(item);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', RandomSkyblock.getInstance().getConfig().getString("messages.chest_spawn")
                                        .replace("${X}", randX + "")
                                        .replace("${Y}", randY + "")
                                        .replace("${Z}", randZ + "")));
                                return;
                            }
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', RandomSkyblock.getInstance().getConfig().getString("messages.spawned")
                                    .replace("${SPAWNED_BLOCK}", mat.name())
                                    .replace("${X}", randX + "")
                                    .replace("${Y}", randY + "")
                                    .replace("${Z}", randZ + "")));

                        } catch (Exception e) {
                            try {
                                Block block = world.getBlockAt(randX, randY, randZ);
                                block.setType(Material.CHEST);
                                Chest chest = (Chest) block.getState();
                                ItemStack item = new ItemStack(mat);
                                item.setAmount(blockRandom.nextInt(config.getInt("random.item_max") - config.getInt("random.item_min")) + config.getInt("random.item_min"));
                                chest.getInventory().addItem(item);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', RandomSkyblock.getInstance().getConfig().getString("messages.chest_spawn")
                                        .replace("${X}", randX + "")
                                        .replace("${Y}", randY + "")
                                        .replace("${Z}", randZ + "")));

                            } catch (Exception exc) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', RandomSkyblock.getInstance().getConfig().getString("messages.error")));
                                exc.printStackTrace();
                            }
                        }

                    }
                    addCount();
                }
            }

        }.runTaskTimer(RandomSkyblock.getInstance(), 10*20, (randomTimer.nextInt(timerHigh-timerLow) + timerLow) * 20);

    }

    private void addCount() {
        this.count += 1;
    }

    private int getCount() {
        return this.count;
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz, Random random){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }



}
