package ru.techno.limewhitelist.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import ru.techno.limewhitelist.PluginProvider;
import ru.techno.limewhitelist.util.ColorUtilities;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void onPlayerLoginAttempt(AsyncPlayerPreLoginEvent event) {
        if (!PluginProvider.enabled) return;

        if (!PluginProvider.whitelist.contains(event.getName().toLowerCase())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, ColorUtilities.convertHexMessageColorsToLegacy(PluginProvider.kickMessage));
        }
    }
}
