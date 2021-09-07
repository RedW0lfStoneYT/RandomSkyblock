package com.redw0lfstone.randomskyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameTimer {

    public GameTimer() {

        Random randomTimer = new Random();
        int timerLow = RandomSkyblock.getInstance().getConfig().getInt("timer.min");
        int timerHigh = RandomSkyblock.getInstance().getConfig().getInt("timer.max");

        Random blockRandom = new Random();
        new BukkitRunnable() {

            @Override
            public void run() {

                Material mat = randomEnum(Material.class, blockRandom);
                World world = Bukkit.getWorld(RandomSkyblock.getInstance().getConfig().getString("world"));
                List<Player> players = world.getPlayers();
                if (players.size() > 0) {
                    Player player = players.get(0);
                    Location location = player.getLocation();
                    int xMin = location.getBlockX() - 50;
                    int xMax = location.getBlockX() + 50;
                    int randX = blockRandom.nextInt(xMax - xMin) + xMin;

                    int yMin = location.getBlockY() - 50;
                    int yMax = location.getBlockY() + 50;
                    int randY = blockRandom.nextInt(yMax - yMin) + yMin;

                    int zMin = location.getBlockZ() - 50;
                    int zMax = location.getBlockZ() + 50;
                    int randZ = blockRandom.nextInt(zMax - zMin) + zMin;

                    try {
                        world.getBlockAt(randX, randY, randZ).setType(mat);
                    } catch (Exception e) {
                        Block block = world.getBlockAt(randX, randY, randZ);
                        block.setType(Material.CHEST);
                        Chest chest = (Chest) block;
                        chest.getInventory().addItem(new ItemStack(mat));
                    }

                }

            }

        }.runTaskTimer(RandomSkyblock.getInstance(), 10*20, (randomTimer.nextInt(timerHigh-timerLow) + timerLow) * 20);

    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz, Random random){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }



}
