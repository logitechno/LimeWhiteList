package ru.techno.limewhitelist;

import me.lucko.commodore.CommodoreProvider;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.CommandExecutor;
import ru.techno.limewhitelist.command.WhitelistCommands;
import ru.techno.limewhitelist.event.PlayerPreLoginListener;

public class LimeWhiteList extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new PlayerPreLoginListener(), this);

        this.getServer().setWhitelistEnforced(false);

        setExecutor("limewhitelist", new WhitelistCommands());

        if (CommodoreProvider.isSupported()) {
            WhitelistCommands.commodore();
        }
    }

    private void setExecutor(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new NullPointerException(String.format("Command %s cannot be null!", name));
        }
        command.setExecutor(executor);
    }
}
