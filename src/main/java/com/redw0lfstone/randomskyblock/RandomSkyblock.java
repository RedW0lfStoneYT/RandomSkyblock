package com.redw0lfstone.randomskyblock;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class RandomSkyblock extends JavaPlugin {


    private static RandomSkyblock instance;

    @Override
    public void onEnable() {
        setupConfig();
        instance = this;
        new GameTimer();
    }



    private void setupConfig() {
        File tempFile = new File(this.getInstance().getDataFolder(), "config.yml");
        if (!tempFile.exists()) {
            this.saveDefaultConfig();
        }
    }

    public static RandomSkyblock getInstance() {
        return instance;
    }

}
