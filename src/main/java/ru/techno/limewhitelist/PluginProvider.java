package ru.techno.limewhitelist;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PluginProvider {
    public static final LimeWhiteList plugin = JavaPlugin.getPlugin(LimeWhiteList.class);

    public static List<String> whitelist = plugin.getConfig().getStringList("whitelist");

    public static boolean enabled = plugin.getConfig().getBoolean("enabled");

    public static String kickMessage = plugin.getConfig().getString("kick_message");
}
