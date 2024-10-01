package ru.techno.limewhitelist.util;

import org.bukkit.command.CommandSender;

public class PermissionCheck {
    public static boolean permissionCheck(CommandSender sender, String perm){
        if (!sender.hasPermission(perm)) {
            sender.sendMessage((ColorUtilities.convertHexMessageColorsToLegacy("&#f52242Недостаточно прав!")));
            return true;
        }
        return false;
    }
}
