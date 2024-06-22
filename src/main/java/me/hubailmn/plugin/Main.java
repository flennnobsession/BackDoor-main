package me.hubailmn.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.Modal;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new Modal(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }
}
