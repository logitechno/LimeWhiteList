package ru.techno.limewhitelist.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;
import ru.techno.limewhitelist.PluginProvider;
import ru.techno.limewhitelist.util.ColorUtilities;
import ru.techno.limewhitelist.util.PermissionCheck;

public class WhitelistCommands implements CommandExecutor {
    private static final String WHITELIST = "whitelist";
    private static final String ENABLED = "enabled";
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }

        String argCommand = args[0];
        switch (argCommand) {
            case "add":
                if (PermissionCheck.permissionCheck(sender, "lwl.manage")) {
                    return true;
                }

                if (args.length != 2) {
                    String errorMessageAdd = "&#f52242Использование: /limewhitelist add <имя_игрока>";
                    sender.sendMessage((ColorUtilities.convertHexMessageColorsToLegacy(errorMessageAdd)));
                    return true;
                }

                String addPlayerName = args[1].toLowerCase();
                if (PluginProvider.whitelist.contains(addPlayerName)) {
                    String presentMessage = String.format("&#22f5b2Игрок %s уже в белом списке", addPlayerName);
                    sender.sendMessage((ColorUtilities.convertHexMessageColorsToLegacy(presentMessage)));
                    return true;
                }

                PluginProvider.whitelist.add(addPlayerName);
                PluginProvider.plugin.getConfig().set(WHITELIST, PluginProvider.whitelist);
                PluginProvider.plugin.saveConfig();

                String addMessage = String.format("&#22f5b2Игрок %s добавлен в белый список", addPlayerName);
                sender.sendMessage((ColorUtilities.convertHexMessageColorsToLegacy(addMessage)));
                break;

            case "remove":
                if (PermissionCheck.permissionCheck(sender, "lwl.manage")) {
                    return true;
                }

                if (args.length != 2) {
                    String errorMessageRemove = "&#f52242Использование: /limewhitelist remove <имя_игрока>";
                    sender.sendMessage((ColorUtilities.convertHexMessageColorsToLegacy(errorMessageRemove)));
                    return true;
                }

                String removePlayerName = args[1].toLowerCase();
                int index = PluginProvider.whitelist.indexOf(removePlayerName);

                if (index == -1) {
                    String absentMessage = String.format("&#f52242Игрока %s нет в белом списке", removePlayerName);
                    sender.sendMessage((ColorUtilities.convertHexMessageColorsToLegacy(absentMessage)));
                    return true;
                }

                PluginProvider.whitelist.remove(index);
                PluginProvider.plugin.getConfig().set(WHITELIST, PluginProvider.whitelist);
                PluginProvider.plugin.saveConfig();

                String removeMessage = String.format("&#f52242Игрок %s удален из белого списка", removePlayerName);
                sender.sendMessage((ColorUtilities.convertHexMessageColorsToLegacy(removeMessage)));
                break;

            case "list":
                PermissionCheck.permissionCheck(sender, "lwl.list");

                String listMessageTitle = "&#22a6f5Игроки в белом списке:";
                sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(listMessageTitle));
                for (String whitelistedPlayerName : PluginProvider.whitelist) {
                    String listMessage = String.format("&#53baf8- %s", whitelistedPlayerName);
                    sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(listMessage));
                }
                break;

            case "on":
                if (PermissionCheck.permissionCheck(sender, "lwl.toggle")) {
                    return true;
                }

                if (PluginProvider.enabled) {
                    String errorMessageOn = "&#22f5b2Белый список уже включен!";
                    sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(errorMessageOn));
                    return true;
                }

                PluginProvider.enabled = true;
                PluginProvider.plugin.getConfig().set(ENABLED, true);
                PluginProvider.plugin.saveConfig();

                String onMessage = "&#22f5b2Белый список был включен!";
                sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(onMessage));
                break;

            case "off":
                if (PermissionCheck.permissionCheck(sender, "lwl.toggle")) {
                    return true;
                }

                if (!PluginProvider.enabled) {
                    String errorMessageOff = "&#f52242Белый список уже выключен!";
                    sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(errorMessageOff));
                    return true;
                }

                PluginProvider.enabled = false;
                PluginProvider.plugin.getConfig().set(ENABLED, false);
                PluginProvider.plugin.saveConfig();

                String affMessage = "&#f52242Белый список был выключен!";
                sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(affMessage));
                break;

            case "reload":
                if (PermissionCheck.permissionCheck(sender, "lwl.reload")) {
                    return true;
                }

                PluginProvider.plugin.reloadConfig();
                PluginProvider.whitelist = PluginProvider.plugin.getConfig().getStringList(WHITELIST);
                PluginProvider.enabled = PluginProvider.plugin.getConfig().getBoolean(ENABLED);
                PluginProvider.kickMessage = PluginProvider.plugin.getConfig().getString("kick_message");

                String reloadMessage = "&#1bfb7fПлагин успешно перезагружен!";
                sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(reloadMessage));
                break;

            case "help":
                if (PermissionCheck.permissionCheck(sender, "lwl.help")) {
                    return true;
                }

                String helpMessage = """
                        &#f91332Список команд плагина:
                        &#b0e332/limewhitelist add &f<ник> &7- добавить игрока в белый список
                        &#b0e332/limewhitelist remove &f<ник> &7- убрать игрока из белого списка
                        &#b0e332/limewhitelist list &7- вывести игроков в белом списке
                        &#b0e332/limewhitelist on &7- включить белый список
                        &#b0e332/limewhitelist off &7- отключить белый список
                        &#b0e332/limewhitelist reload &7- перезагрузить плагин
                        &#b0e332/limewhitelist help &7- вывести это сообщение""";
                sender.sendMessage(ColorUtilities.convertHexMessageColorsToLegacy(helpMessage));
                break;

            default:
                return false;
        }
        return true;
    }

    public static void commodore() {
        final PluginCommand command = PluginProvider.plugin.getCommand("limewhitelist");
        assert command != null;

        final Commodore commodore = CommodoreProvider.getCommodore(PluginProvider.plugin);

        registerCompletions(commodore, command);
    }

    private static void registerCompletions(Commodore commodore, PluginCommand command) {
        commodore.register(command, LiteralArgumentBuilder.literal("limewhitelist")
                .then(LiteralArgumentBuilder.literal("add")
                        .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word())))
                .then(LiteralArgumentBuilder.literal("remove")
                        .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word())))
                .then(LiteralArgumentBuilder.literal("list"))
                .then(LiteralArgumentBuilder.literal("on"))
                .then(LiteralArgumentBuilder.literal("off"))
                .then(LiteralArgumentBuilder.literal("reload"))
                .then(LiteralArgumentBuilder.literal("help"))
        );
    }
}
